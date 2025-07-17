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
import com.essalud.sispoi.dto.OperationalActivityDTO;
import com.essalud.sispoi.dto.OperationalActivitySearchDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.OperationalActivity;
import com.essalud.sispoi.service.IOperationalActivityService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/operational-activity")
public class OperationalActivityController {

    @Autowired
    private IOperationalActivityService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<OperationalActivityDTO>> findAll() {
        List<OperationalActivityDTO> list = service.findAll().stream().map(p -> mapper.map(p, OperationalActivityDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationalActivityDTO> findById(@PathVariable Integer id) {

        OperationalActivityDTO dtoResponse;
        OperationalActivity obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, OperationalActivityDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OperationalActivityDTO> save(@Valid @RequestBody OperationalActivityDTO dto) {
        OperationalActivity p = service.save(mapper.map(dto, OperationalActivity.class));
        OperationalActivityDTO dtoResponse = (mapper.map(p, OperationalActivityDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<OperationalActivity> update(@Valid @RequestBody OperationalActivityDTO dto) {
        OperationalActivity existing = service.findById(dto.getIdOperationalActivity());

        if (existing == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdOperationalActivity());
        }

        OperationalActivity updated = mapper.map(dto, OperationalActivity.class);
        updated.setGoals(existing.getGoals());
        updated.setExecutedGoals(existing.getExecutedGoals());
        updated.setCreateTime(existing.getCreateTime());

        service.update(updated); // ✅ ACTUALIZACIÓN REAL

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        OperationalActivity obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/search")
    public ResponseEntity<List<OperationalActivityDTO>> findByFormulation(@Valid @RequestBody OperationalActivitySearchDTO request) {
        Formulation formulation = mapper.map(request.getFormulation(), Formulation.class);

        List<OperationalActivity> activities = service.findByFormulation(formulation);
        List<OperationalActivityDTO> result = activities.stream()
            .map(oa -> mapper.map(oa, OperationalActivityDTO.class))
            .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/higher-correlative-code/cost-center/{idCostCenter}")
    public ResponseEntity<String> getHigherCorrelativeCodeByCostCenter(@PathVariable Integer idCostCenter) {
        String higherCode = service.getHigherCorrelativeCodeByCostCenter(idCostCenter);
        return new ResponseEntity<>(higherCode, HttpStatus.OK);
    }

}
