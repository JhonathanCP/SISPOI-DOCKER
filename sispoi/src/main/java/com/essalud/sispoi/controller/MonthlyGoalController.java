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

import com.essalud.sispoi.dto.MonthlyGoalDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.MonthlyGoal;
import com.essalud.sispoi.service.IMonthlyGoalService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/monthly-goal")
public class MonthlyGoalController {

    @Autowired
    private IMonthlyGoalService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<MonthlyGoalDTO>> findAll() {
        List<MonthlyGoalDTO> list = service.findAll().stream().map(p -> mapper.map(p, MonthlyGoalDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonthlyGoalDTO> findById(@PathVariable Integer id) {

        MonthlyGoalDTO dtoResponse;
        MonthlyGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, MonthlyGoalDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MonthlyGoalDTO> save(@Valid @RequestBody MonthlyGoalDTO dto) {
        MonthlyGoal p = service.save(mapper.map(dto, MonthlyGoal.class));
        MonthlyGoalDTO dtoResponse = (mapper.map(p, MonthlyGoalDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MonthlyGoal> update(@Valid @RequestBody MonthlyGoalDTO dto) {
        MonthlyGoal obj = service.findById(dto.getIdMonthlyGoal());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdMonthlyGoal());
        }
        service.update(mapper.map(dto, MonthlyGoal.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        MonthlyGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
