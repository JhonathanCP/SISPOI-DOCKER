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

import com.essalud.sispoi.dto.ActivityFamilyDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ActivityFamily;
import com.essalud.sispoi.service.IActivityFamilyService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/activity-family")
public class ActivityFamilyController {

    @Autowired
    private IActivityFamilyService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ActivityFamilyDTO>> findAll() {
        List<ActivityFamilyDTO> list = service.findAll().stream().map(p -> mapper.map(p, ActivityFamilyDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityFamilyDTO> findById(@PathVariable Integer id) {

        ActivityFamilyDTO dtoResponse;
        ActivityFamily obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ActivityFamilyDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActivityFamilyDTO> save(@Valid @RequestBody ActivityFamilyDTO dto) {
        ActivityFamily p = service.save(mapper.map(dto, ActivityFamily.class));
        ActivityFamilyDTO dtoResponse = (mapper.map(p, ActivityFamilyDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ActivityFamily> update(@Valid @RequestBody ActivityFamilyDTO dto) {
        ActivityFamily obj = service.findById(dto.getIdActivityFamily());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdActivityFamily());
        }
        service.update(mapper.map(dto, ActivityFamily.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ActivityFamily obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
