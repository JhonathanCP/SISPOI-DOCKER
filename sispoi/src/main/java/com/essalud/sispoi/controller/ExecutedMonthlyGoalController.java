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

import com.essalud.sispoi.dto.ExecutedMonthlyGoalDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ExecutedMonthlyGoal;
import com.essalud.sispoi.service.IExecutedMonthlyGoalService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/executed-monthly-goal")
public class ExecutedMonthlyGoalController {

    @Autowired
    private IExecutedMonthlyGoalService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExecutedMonthlyGoalDTO>> findAll() {
        List<ExecutedMonthlyGoalDTO> list = service.findAll().stream().map(p -> mapper.map(p, ExecutedMonthlyGoalDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecutedMonthlyGoalDTO> findById(@PathVariable Integer id) {

        ExecutedMonthlyGoalDTO dtoResponse;
        ExecutedMonthlyGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ExecutedMonthlyGoalDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExecutedMonthlyGoalDTO> save(@Valid @RequestBody ExecutedMonthlyGoalDTO dto) {
        ExecutedMonthlyGoal p = service.save(mapper.map(dto, ExecutedMonthlyGoal.class));
        ExecutedMonthlyGoalDTO dtoResponse = (mapper.map(p, ExecutedMonthlyGoalDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ExecutedMonthlyGoal> update(@Valid @RequestBody ExecutedMonthlyGoalDTO dto) {
        ExecutedMonthlyGoal obj = service.findById(dto.getIdExecutedMonthlyGoal());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdExecutedMonthlyGoal());
        }
        dto.setCreateTime(obj.getCreateTime());
        service.update(mapper.map(dto, ExecutedMonthlyGoal.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ExecutedMonthlyGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
