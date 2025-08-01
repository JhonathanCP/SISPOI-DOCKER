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

import com.essalud.sispoi.dto.FormulationDTO;
import com.essalud.sispoi.dto.FormulationSearchDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.service.IFormulationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/formulation")
public class FormulationController {

    @Autowired
    private IFormulationService service;

    @Autowired
    private  ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<FormulationDTO>> findAll() {
        List<FormulationDTO> list = service.findAll().stream().map(p -> mapper.map(p, FormulationDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormulationDTO> findById(@PathVariable Integer id) {

        FormulationDTO dtoResponse;
        Formulation obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, FormulationDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FormulationDTO> save(@Valid @RequestBody FormulationDTO dto) {
        Formulation p = service.save(mapper.map(dto, Formulation.class));
        FormulationDTO dtoResponse = (mapper.map(p, FormulationDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Formulation> update(@Valid @RequestBody FormulationDTO dto) {
        Formulation obj = service.findById(dto.getIdFormulation());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdFormulation());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        Formulation obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/search")
    public ResponseEntity<List<FormulationDTO>> findByDependencyAndYear(@Valid @RequestBody FormulationSearchDTO request) {
        // Mapeo explícito de DependencyDTO a Dependency
        Dependency dependency = mapper.map(request.getDependency(), Dependency.class);

        List<Formulation> formulations = service.findByDependencyAndYear(dependency, request.getYear());
        List<FormulationDTO> list = formulations.stream()
            .map(f -> mapper.map(f, FormulationDTO.class))
            .collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add-modification/{idOriginalFormulation}/{newQuarter}")
    public ResponseEntity<FormulationDTO> addModification(
            @PathVariable("idOriginalFormulation") Integer idOriginalFormulation,
            @PathVariable("newQuarter") Integer newQuarter) {

        // Validate quarter input (1 to 4)
        if (newQuarter < 1 || newQuarter > 5) {
            throw new IllegalArgumentException("Quarter must be between 1 and 4.");
        }

        Formulation newFormulation = service.addModification(idOriginalFormulation, newQuarter);
        FormulationDTO dtoResponse = mapper.map(newFormulation, FormulationDTO.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED); // Use CREATED status for new resource
    }

    @PostMapping("/add-modificationpe/{idOriginalFormulation}/{newMonth}")
    public ResponseEntity<FormulationDTO> addModificationPe(
            @PathVariable("idOriginalFormulation") Integer idOriginalFormulation,
            @PathVariable("newMonth") Integer newMonth) {

        // Validate month input (1 to 12)
        if (newMonth < 1 || newMonth > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }

        Formulation newFormulation = service.addModificationPe(idOriginalFormulation, newMonth);
        FormulationDTO dtoResponse = mapper.map(newFormulation, FormulationDTO.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED); // Use CREATED status for new resource
    }       

}
