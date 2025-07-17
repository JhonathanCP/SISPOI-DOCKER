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

import com.essalud.sispoi.dto.ActivityDetailDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.ActivityDetail;
import com.essalud.sispoi.service.IActivityDetailService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/activity-detail")
public class ActivityDetailController {

    @Autowired
    private IActivityDetailService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ActivityDetailDTO>> findAll() {
        List<ActivityDetailDTO> list = service.findAll().stream().map(p -> mapper.map(p, ActivityDetailDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDetailDTO> findById(@PathVariable Integer id) {

        ActivityDetailDTO dtoResponse;
        ActivityDetail obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            dtoResponse = mapper.map(obj, ActivityDetailDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActivityDetailDTO> save(@Valid @RequestBody ActivityDetailDTO dto) {
        ActivityDetail p = service.save(mapper.map(dto, ActivityDetail.class));
        ActivityDetailDTO dtoResponse = (mapper.map(p, ActivityDetailDTO.class));; 
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ActivityDetail> update(@Valid @RequestBody ActivityDetailDTO dto) {
        ActivityDetail obj = service.findById(dto.getIdActivityDetail());
        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + dto.getIdActivityDetail());
        }
        dto.setCreateTime(obj.getCreateTime());
        return new ResponseEntity<>(service.update(mapper.map(dto, ActivityDetail.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        ActivityDetail obj = service.findById(id);

        if(obj == null){
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }else{
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
