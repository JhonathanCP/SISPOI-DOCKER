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

import com.essalud.sispoi.dto.MinMaxYearsDTO;
import com.essalud.sispoi.dto.StrategicObjectiveDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.StrategicObjective;
import com.essalud.sispoi.service.IStrategicObjectiveService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/strategic-objective")
public class StrategicObjectiveController {

    @Autowired
    private IStrategicObjectiveService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<StrategicObjectiveDTO>> findAll() {
        List<StrategicObjectiveDTO> list = service.findAll().stream().map(p -> mapper.map(p, StrategicObjectiveDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrategicObjectiveDTO> findById(@PathVariable Integer id) {

        StrategicObjectiveDTO dtoResponse;
        StrategicObjective obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, StrategicObjectiveDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StrategicObjectiveDTO> save(@Valid @RequestBody StrategicObjectiveDTO dto) {
        StrategicObjective p = service.save(mapper.map(dto, StrategicObjective.class));
        StrategicObjectiveDTO dtoResponse = (mapper.map(p, StrategicObjectiveDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<StrategicObjective> update(@Valid @RequestBody StrategicObjectiveDTO dto) {
        StrategicObjective obj = service.findById(dto.getIdStrategicObjective());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdStrategicObjective());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, StrategicObjective.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        StrategicObjective obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/years-range")
    public ResponseEntity<MinMaxYearsDTO> getMinMaxYears() {
        MinMaxYearsDTO minMaxYears = service.getMinMaxYears();
        return new ResponseEntity<>(minMaxYears, HttpStatus.OK);
    }

}
