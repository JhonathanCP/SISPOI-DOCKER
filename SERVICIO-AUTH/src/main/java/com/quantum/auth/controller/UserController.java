package com.quantum.auth.controller;

import org.apache.commons.lang3.RandomStringUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quantum.auth.service.IUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.quantum.auth.dto.ChangePasswordDTO;
import com.quantum.auth.dto.DependencyDTO;
import com.quantum.auth.dto.RoleDTO;
import com.quantum.auth.dto.UserDTO;
import com.quantum.auth.exception.ModelNotFoundException;
import com.quantum.auth.kafka.EmailRequest;
import com.quantum.auth.kafka.producer.KafkaProducerService;
import com.quantum.auth.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll().stream().map(p -> mapper.map(p, UserDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        UserDTO dtoResponse;
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, UserDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UserDTO dto) {
        // Generar contraseña aleatoria
        String rawPassword = RandomStringUtils.randomAlphanumeric(6);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        dto.setPassword(encodedPassword);

        User savedUser = userService.save(mapper.map(dto, User.class));

        // Notificar a Kafka
        dto.setIdUser(savedUser.getIdUser());
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", dto.getUsername());
        variables.put("password", rawPassword); // ⚠️ Solo si lo vas a mandar por correo

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(dto.getEmail());
        emailRequest.setSubject("Tu cuenta ha sido creada");
        emailRequest.setTemplateName("new-user.html");
        emailRequest.setVariables(variables);

        kafkaProducerService.sendEmailEvent(emailRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(savedUser.getIdUser())
                            .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
        // Verificar que el usuario exista
        User obj = userService.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad User
        User user = mapper.map(dto, User.class);

        // Asignar el ID desde la URL al objeto mapeado
        user.setIdUser(id);

        // Actualizar el usuario
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }


    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            userService.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        String rawPassword = RandomStringUtils.randomAlphanumeric(6);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userService.update(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("password", rawPassword);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Tu contraseña ha sido restablecida");
        emailRequest.setTemplateName("reset-password.html");
        emailRequest.setVariables(variables);

        kafkaProducerService.sendEmailEvent(emailRequest);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @RequestBody ChangePasswordDTO dto) {
        User user = userService.findById(id);
        if (user == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);
        userService.update(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("password", dto.getPassword());

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Tu contraseña ha sido cambiada");
        emailRequest.setTemplateName("change-password.html");
        emailRequest.setVariables(variables);

        kafkaProducerService.sendEmailEvent(emailRequest);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/add-role")
    public ResponseEntity<Void> addRole(@PathVariable Integer id, @RequestBody RoleDTO dto) {
        userService.addRole(id, dto.getIdRole());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/remove-role")
    public ResponseEntity<Void> removeRole(@PathVariable Integer id, @RequestBody RoleDTO dto) {
        userService.removeRole(id, dto.getIdRole());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/add-dependency")
    public ResponseEntity<Void> addDependency(@PathVariable Integer id, @RequestBody DependencyDTO dto) {
        userService.addDependency(id, dto.getIdDependency());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{id}/remove-dependency")
    public ResponseEntity<Void> removeDependency(@PathVariable Integer id, @RequestBody DependencyDTO dto) {
        userService.removeDependency(id, dto.getIdDependency());
        return ResponseEntity.noContent().build();
    }

}
