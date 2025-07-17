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
import com.essalud.sispoi.dto.BudgetItemDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.BudgetItem;
import com.essalud.sispoi.service.IBudgetItemService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/budget-item")
public class BudgetItemController {

    @Autowired
    private IBudgetItemService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<BudgetItemDTO>> findAll() {
        List<BudgetItemDTO> list = service.findAll().stream().map(p -> mapper.map(p, BudgetItemDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetItemDTO> findById(@PathVariable Integer id) {

        BudgetItemDTO dtoResponse;
        BudgetItem obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, BudgetItemDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BudgetItemDTO> save(@Valid @RequestBody BudgetItemDTO dto) {
        BudgetItem p = service.save(mapper.map(dto, BudgetItem.class));
        BudgetItemDTO dtoResponse = (mapper.map(p, BudgetItemDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<BudgetItem> update(@Valid @RequestBody BudgetItemDTO dto) {
        BudgetItem obj = service.findById(dto.getIdBudgetItem());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdBudgetItem());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, BudgetItem.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        BudgetItem obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
