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

import com.essalud.sispoi.dto.StrategicActionDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.StrategicAction;
import com.essalud.sispoi.service.IStrategicActionService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/strategic-action")
public class StrategicActionController {

    @Autowired
    private IStrategicActionService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<StrategicActionDTO>> findAll() {
        List<StrategicActionDTO> list = service.findAll().stream().map(p -> mapper.map(p, StrategicActionDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrategicActionDTO> findById(@PathVariable Integer id) {

        StrategicActionDTO dtoResponse;
        StrategicAction obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, StrategicActionDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StrategicActionDTO> save(@Valid @RequestBody StrategicActionDTO dto) {
        StrategicAction p = service.save(mapper.map(dto, StrategicAction.class));
        StrategicActionDTO dtoResponse = (mapper.map(p, StrategicActionDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<StrategicAction> update(@Valid @RequestBody StrategicActionDTO dto) {
        StrategicAction obj = service.findById(dto.getIdStrategicAction());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdStrategicAction());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, StrategicAction.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        StrategicAction obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
