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

import com.essalud.sispoi.dto.BudgetCategoryDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.BudgetCategory;
import com.essalud.sispoi.service.IBudgetCategoryService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/budget-category")
public class BudgetCategoryController {

    @Autowired
    private IBudgetCategoryService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<BudgetCategoryDTO>> findAll() {
        List<BudgetCategoryDTO> list = service.findAll().stream().map(p -> mapper.map(p, BudgetCategoryDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetCategoryDTO> findById(@PathVariable Integer id) {

        BudgetCategoryDTO dtoResponse;
        BudgetCategory obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, BudgetCategoryDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BudgetCategoryDTO> save(@Valid @RequestBody BudgetCategoryDTO dto) {
        BudgetCategory p = service.save(mapper.map(dto, BudgetCategory.class));
        BudgetCategoryDTO dtoResponse = (mapper.map(p, BudgetCategoryDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<BudgetCategory> update(@Valid @RequestBody BudgetCategoryDTO dto) {
        BudgetCategory obj = service.findById(dto.getIdBudgetCategory());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdBudgetCategory());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, BudgetCategory.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        BudgetCategory obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
