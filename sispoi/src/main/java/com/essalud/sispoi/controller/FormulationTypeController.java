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

import com.essalud.sispoi.dto.FormulationTypeDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.FormulationType;
import com.essalud.sispoi.service.IFormulationTypeService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/formulation-type")
public class FormulationTypeController {

    @Autowired
    private IFormulationTypeService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<FormulationTypeDTO>> findAll() {
        List<FormulationTypeDTO> list = service.findAll().stream().map(p -> mapper.map(p, FormulationTypeDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormulationTypeDTO> findById(@PathVariable Integer id) {

        FormulationTypeDTO dtoResponse;
        FormulationType obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, FormulationTypeDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FormulationTypeDTO> save(@Valid @RequestBody FormulationTypeDTO dto) {
        FormulationType p = service.save(mapper.map(dto, FormulationType.class));
        FormulationTypeDTO dtoResponse = (mapper.map(p, FormulationTypeDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<FormulationType> update(@Valid @RequestBody FormulationTypeDTO dto) {
        FormulationType obj = service.findById(dto.getIdFormulationType());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdFormulationType());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        FormulationType obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
