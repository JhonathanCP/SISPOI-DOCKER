package com.essalud.sispoi.controller;

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

import com.essalud.sispoi.dto.ManagementCenterDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ManagementCenter;
import com.essalud.sispoi.service.IManagementCenterService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/management-center")
public class ManagementCenterController {

    @Autowired
    private IManagementCenterService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ManagementCenterDTO>> findAll() {
        List<ManagementCenterDTO> list = service.findAll().stream().map(p -> mapper.map(p, ManagementCenterDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagementCenterDTO> findById(@PathVariable Integer id) {

        ManagementCenterDTO dtoResponse;
        ManagementCenter obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ManagementCenterDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ManagementCenterDTO> save(@Valid @RequestBody ManagementCenterDTO dto) {
        ManagementCenter p = service.save(mapper.map(dto, ManagementCenter.class));
        ManagementCenterDTO dtoResponse = (mapper.map(p, ManagementCenterDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ManagementCenter> update(@Valid @RequestBody ManagementCenterDTO dto) {
        ManagementCenter obj = service.findById(dto.getIdManagementCenter());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdManagementCenter());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, ManagementCenter.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ManagementCenter obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
