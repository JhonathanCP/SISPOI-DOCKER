package com.quantum.auth.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quantum.auth.service.ILoginAuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.quantum.auth.dto.LoginAuditDTO;
import com.quantum.auth.exception.ModelNotFoundException;
import com.quantum.auth.model.LoginAudit;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/login-audits")
public class LoginAuditController {

    @Autowired
    private ILoginAuditService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<LoginAuditDTO>> findAll() {
        List<LoginAuditDTO> list = service.findAll().stream().map(p -> mapper.map(p, LoginAuditDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginAuditDTO> findById(@PathVariable Integer id) {
        LoginAuditDTO dtoResponse;
        LoginAudit obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, LoginAuditDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody LoginAuditDTO dto, HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        dto.setIp(ip);

        LoginAudit p = service.save(mapper.map(dto, LoginAudit.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdLoginAudit()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoginAudit> update(@PathVariable Integer id, @Valid @RequestBody LoginAuditDTO dto) {
        // Verificar que el usuario exista
        LoginAudit obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad LoginAudit
        LoginAudit LoginAudit = mapper.map(dto, LoginAudit.class);

        // Asignar el ID desde la URL al objeto mapeado
        LoginAudit.setIdLoginAudit(id);

        // Actualizar el usuario
        return new ResponseEntity<>(service.update(LoginAudit), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        LoginAudit obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}