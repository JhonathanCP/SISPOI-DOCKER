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

import com.essalud.sispoi.dto.ExecutedGoalDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ExecutedGoal;
import com.essalud.sispoi.service.IExecutedGoalService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/executed-goal")
public class ExecutedGoalController {

    @Autowired
    private IExecutedGoalService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExecutedGoalDTO>> findAll() {
        List<ExecutedGoalDTO> list = service.findAll().stream().map(p -> mapper.map(p, ExecutedGoalDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecutedGoalDTO> findById(@PathVariable Integer id) {

        ExecutedGoalDTO dtoResponse;
        ExecutedGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ExecutedGoalDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExecutedGoalDTO> save(@Valid @RequestBody ExecutedGoalDTO dto) {
        ExecutedGoal p = service.save(mapper.map(dto, ExecutedGoal.class));
        ExecutedGoalDTO dtoResponse = (mapper.map(p, ExecutedGoalDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ExecutedGoal> update(@Valid @RequestBody ExecutedGoalDTO dto) {
        ExecutedGoal obj = service.findById(dto.getIdExecutedGoal());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdExecutedGoal());
        }
        service.update(mapper.map(dto, ExecutedGoal.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ExecutedGoal obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
