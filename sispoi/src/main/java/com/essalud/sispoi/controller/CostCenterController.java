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

import com.essalud.sispoi.dto.CostCenterDTO;

import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.CostCenter;

import com.essalud.sispoi.service.ICostCenterService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/cost-center")
public class CostCenterController {

    @Autowired
    private ICostCenterService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<CostCenterDTO>> findAll() {
        List<CostCenterDTO> list = service.findAll().stream().map(p -> mapper.map(p, CostCenterDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostCenterDTO> findById(@PathVariable Integer id) {

        CostCenterDTO dtoResponse;
        CostCenter obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, CostCenterDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CostCenterDTO> save(@Valid @RequestBody CostCenterDTO dto) {
        CostCenter p = service.save(mapper.map(dto, CostCenter.class));
        CostCenterDTO dtoResponse = (mapper.map(p, CostCenterDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CostCenter> update(@Valid @RequestBody CostCenterDTO dto) {
        CostCenter obj = service.findById(dto.getIdCostCenter());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdCostCenter());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, CostCenter.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        CostCenter obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
