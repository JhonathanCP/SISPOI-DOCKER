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

import com.essalud.sispoi.dto.ExpenseTypeDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ExpenseType;
import com.essalud.sispoi.service.IExpenseTypeService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/expense-type")
public class ExpenseTypeController {

    @Autowired
    private IExpenseTypeService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExpenseTypeDTO>> findAll() {
        List<ExpenseTypeDTO> list = service.findAll().stream().map(p -> mapper.map(p, ExpenseTypeDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTypeDTO> findById(@PathVariable Integer id) {

        ExpenseTypeDTO dtoResponse;
        ExpenseType obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ExpenseTypeDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExpenseTypeDTO> save(@Valid @RequestBody ExpenseTypeDTO dto) {
        ExpenseType p = service.save(mapper.map(dto, ExpenseType.class));
        ExpenseTypeDTO dtoResponse = (mapper.map(p, ExpenseTypeDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ExpenseType> update(@Valid @RequestBody ExpenseTypeDTO dto) {
        ExpenseType obj = service.findById(dto.getIdExpenseType());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdExpenseType());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, ExpenseType.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ExpenseType obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
