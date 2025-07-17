package com.quantum.auth.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quantum.auth.dto.DependencyDTO;
import com.quantum.auth.exception.ModelNotFoundException;
import com.quantum.auth.model.Dependency;
import com.quantum.auth.service.IDependencyService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/dependency")
public class DependencyController {

    @Autowired
    private IDependencyService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<DependencyDTO>> findAll() {
        List<DependencyDTO> list = service.findAll().stream().map(p -> mapper.map(p, DependencyDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DependencyDTO> findById(@PathVariable Integer id) {

        DependencyDTO dtoResponse;
        Dependency obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, DependencyDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody DependencyDTO dto) {
        Dependency p = service.save(mapper.map(dto, Dependency.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdDependency()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Dependency> update(@Valid @RequestBody DependencyDTO dto) {
        Dependency obj = service.findById(dto.getIdDependency());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdDependency());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, Dependency.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        Dependency obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
