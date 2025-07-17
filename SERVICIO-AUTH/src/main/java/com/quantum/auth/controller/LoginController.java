package com.quantum.auth.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quantum.auth.dto.LoginAuditDTO;
import com.quantum.auth.dto.UserDTO;
import com.quantum.auth.exception.ModelNotFoundException;
import com.quantum.auth.model.LoginAudit;
import com.quantum.auth.model.User;
import com.quantum.auth.security.JwtRequest;
import com.quantum.auth.security.JwtResponse;
import com.quantum.auth.security.JwtTokenUtil;
import com.quantum.auth.security.JwtUserDetailsService;
import com.quantum.auth.service.ILoginAuditService;
import com.quantum.auth.service.IUserService;
import com.quantum.auth.service.impl.RecaptchaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final RecaptchaService recaptchaService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ILoginAuditService loginAuditService;

    @Autowired
    private ModelMapper mapper;

    @Value("${auth.recaptcha.enabled:false}")
    private boolean isRecaptchaEnabled;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req, HttpServletRequest request) throws Exception {
        LoginAuditDTO loginAuditDTO = new LoginAuditDTO();
        loginAuditDTO.setUsername(req.getUsername().toLowerCase());
        loginAuditDTO.setAccessDateTime(java.time.LocalDateTime.now());

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        loginAuditDTO.setIp(ip);

        try {
            // Validar reCAPTCHA si está habilitado
            if (isRecaptchaEnabled) {
                if (!recaptchaService.verifyRecaptcha(req.getRecaptchaToken())) {
                    loginAuditDTO.setSuccess(false);
                    loginAuditDTO.setUser(null);
                    loginAuditService.save(mapper.map(loginAuditDTO, LoginAudit.class));
                    return ResponseEntity.badRequest().body(new JwtResponse(null, "Invalid reCAPTCHA token"));
                }
            }

            // Obtener usuario
            User obj = userService.findOneByUsername(req.getUsername().toLowerCase());
            UserDTO user = obj != null ? mapper.map(obj, UserDTO.class) : null;

            if (obj == null) {
                loginAuditDTO.setSuccess(false);
                loginAuditDTO.setUser(null);
                loginAuditService.save(mapper.map(loginAuditDTO, LoginAudit.class));
                return ResponseEntity.badRequest().body(new JwtResponse(null, "USER NOT FOUND"));
            }

            loginAuditDTO.setUser(user);
            authenticate(req.getUsername().toLowerCase(), req.getPassword());

            loginAuditDTO.setSuccess(true);
            loginAuditService.save(mapper.map(loginAuditDTO, LoginAudit.class));

            final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getUsername().toLowerCase());
            final String accessToken = jwtTokenUtil.generateToken(userDetails);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        } catch (Exception e) {
            e.printStackTrace(); // <-- Esto imprimirá el error real en consola/log
            loginAuditDTO.setSuccess(false);
            loginAuditService.save(mapper.map(loginAuditDTO, LoginAudit.class));
            throw new ModelNotFoundException("LOGIN FAILED: " + e.getMessage()); // <- para que devuelva el mensaje real
        }

    }

    // Endpoint para refrescar el token
    @PostMapping("/refresh-tokens")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody Map<String, String> tokenRequest) throws Exception {
        // Extraer el refresh token del cuerpo de la solicitud
        String accessToken = tokenRequest.get("access_token");
        String refreshToken = tokenRequest.get("refresh_token");

        // Validar si el refresh token es nulo o está vacío
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Refresh token is missing"));
        }

        // Validar si es un Refresh Token
        if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Invalid token: Expected refresh token"));
        }

        // Validar si el refresh token ha expirado
        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Refresh token has expired"));
        }
        
        // Extraer el nombre de usuario desde el refresh token
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        // INVALIDAR EL ACCESS TOKEN Y EL REFRESH TOKEN
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            // Invalidar el access token en la base de datos
            jwtTokenUtil.revokeToken(accessToken);
        }

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            // Invalidar el refresh token en la base de datos
            jwtTokenUtil.revokeToken(refreshToken);
        }

        // Cargar los detalles del usuario
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

        // Generar un nuevo access token usando los detalles del usuario
        final String newAccessToken = jwtTokenUtil.generateToken(userDetails);

        // Generar un nuevo refresh token
        final String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Devolver el nuevo access token junto con el nuevo refresh token
        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
    }

    // Endpoint para dar de baja el token
    @PostMapping("/logout-tokens")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> tokenRequest) throws Exception{
        String accessToken = tokenRequest.get("access_token");
        String refreshToken = tokenRequest.get("refresh_token");

        if (accessToken != null && !accessToken.trim().isEmpty()) {
            // Invalidar el access token en la base de datos
            jwtTokenUtil.revokeToken(accessToken);
        }

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            // Invalidar el refresh token en la base de datos
            jwtTokenUtil.revokeToken(refreshToken);
        }

        return ResponseEntity.ok("Logged out successfully");
    }    

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("Usuario deshabilitado", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales inválidas", e);
        }
    }
}