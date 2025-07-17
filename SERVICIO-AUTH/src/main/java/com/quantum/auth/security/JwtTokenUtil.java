package com.quantum.auth.security;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.quantum.auth.model.Token;
import com.quantum.auth.model.User;
import com.quantum.auth.repo.ITokenRepo;
import com.quantum.auth.repo.IUserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    //private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 horas
    private final long JWT_TOKEN_VALIDITY = 10 * 24 * 60 * 60 * 1000; // 10 días
    private final long REFRESH_TOKEN_VALIDITY = 10 * 24 * 60 * 60 * 1000; // 10 días

    @Value("${jwt.secret}")
    private String secret;

    private final ITokenRepo repo;

    private final IUserRepo userRepo;

    // Generar el Access Token y guardarlo en la base de datos
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        User user = userRepo.findOneByUsername(userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","))); // ADMIN,USER,DBA
        claims.put("type", "access");
        claims.put("id", user.getIdUser()); // <-- Se agrega el idUser al token
        claims.put(
            "dependencies",
            user.getDependencies().stream()
                .map(dep -> dep.getIdDependency()) // o dep.getNombre() si quieres el nombre
                .collect(Collectors.toList())
        );
        String token = doGenerateToken(claims, userDetails.getUsername(), JWT_TOKEN_VALIDITY);

        // Guardar el token en la base de datos
        saveToken(token, userDetails.getUsername(), "access", JWT_TOKEN_VALIDITY);

        return token;
    }

    // Generar el Refresh Token y guardarlo en la base de datos
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        String token = doGenerateToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);

        // Guardar el token en la base de datos
        saveToken(token, userDetails.getUsername(), "refresh", REFRESH_TOKEN_VALIDITY);

        return token;
    }

    // Guardar el token en la base de datos
    private void saveToken(String token, String username, String tokenType, long validity) {
        Token newToken = new Token();
        newToken.setToken(token);
        newToken.setUsername(username);
        newToken.setTokenType(tokenType);
        long validitySeconds = validity / 1000;
        newToken.setExpiration(LocalDateTime.now().plusSeconds(validitySeconds));
        newToken.setIssuedAt(LocalDateTime.now());
        newToken.setIsValid(true);  // Todos los tokens son válidos al momento de su emisión

        repo.save(newToken);
    }

    // Generación del token con tiempo de validez personalizado
    @SuppressWarnings("deprecation")
    private String doGenerateToken(Map<String, Object> claims, String username, long validity) {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key)
                .compact();
    }

    //utils
    public Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    // Obtener una Claim específica del token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Obtener la fecha de expiración del token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Verificar si el token ha expirado
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Validar si es un Access Token
    public Boolean isAccessToken(String token) {
        String tokenType = getClaimFromToken(token, claims -> claims.get("type", String.class));
        return "access".equals(tokenType);
    }

    // Validar si es un Refresh Token
    public Boolean isRefreshToken(String token) {
        String tokenType = getClaimFromToken(token, claims -> claims.get("type", String.class));
        return "refresh".equals(tokenType);
    }

    // Validar el Access Token
    public Boolean validateAccessToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token) && isAccessToken(token));
    }

    // Validar si un token es válido desde la base de datos
    public Boolean validateTokenFromDB(String token, UserDetails userDetails) {
        Optional<Token> tokenEntity = repo.findByToken(token);
        
        if (tokenEntity.isEmpty() || !tokenEntity.get().getIsValid()) {
            return false;  // Si el token no existe o no es válido, la validación falla
        }
        
        final String username = getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void revokeToken(String token) {
        Optional<Token> tokenEntity = repo.findByToken(token);
        tokenEntity.ifPresent(t -> {
            t.setIsValid(false);
            repo.save(t);
        });
    }

    public void deleteToken(String token) {
        repo.deleteByToken(token);
    }
    
    

}
