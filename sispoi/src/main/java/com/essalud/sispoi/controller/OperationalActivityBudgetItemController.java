package com.essalud.sispoi.controller;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.sispoi.dto.OperationalActivityBudgetItemDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.OperationalActivityBudgetItem;
import com.essalud.sispoi.service.IOperationalActivityBudgetItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/operational-activity-budget-item")
public class OperationalActivityBudgetItemController {

    @Autowired
    private IOperationalActivityBudgetItemService service;

    @Autowired
    private ModelMapper mapper;

    // Obtener todos los registros
    @GetMapping
    public ResponseEntity<List<OperationalActivityBudgetItemDTO>> findAll() {
        List<OperationalActivityBudgetItemDTO> list = service.findAll().stream()
            .map(p -> mapper.map(p, OperationalActivityBudgetItemDTO.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Obtener todos los registros de una actividad operativa
    @GetMapping("/operational-activity/{idOperationalActivity}")
    public ResponseEntity<List<OperationalActivityBudgetItemDTO>> findByOperationalActivity(
            @PathVariable Integer idOperationalActivity) {
        List<OperationalActivityBudgetItemDTO> list = service.findByOperationalActivity(idOperationalActivity).stream()
            .map(p -> mapper.map(p, OperationalActivityBudgetItemDTO.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Obtener un registro específico por ambos IDs
    @GetMapping("/{idOperationalActivity}/{idBudgetItem}")
    public ResponseEntity<OperationalActivityBudgetItemDTO> findById(
            @PathVariable Integer idOperationalActivity,
            @PathVariable Integer idBudgetItem,
            @PathVariable Integer orderItem) {

        OperationalActivityBudgetItem obj = service.findByIds(idOperationalActivity, idBudgetItem, orderItem);

        if (obj == null) {
            throw new ModelNotFoundException("IDs DO NOT EXIST: " + idOperationalActivity + ", " + idBudgetItem);
        }
        OperationalActivityBudgetItemDTO dtoResponse = mapper.map(obj, OperationalActivityBudgetItemDTO.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    // Crear un nuevo registro
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody OperationalActivityBudgetItemDTO dto) {
        OperationalActivityBudgetItem entity = mapper.map(dto, OperationalActivityBudgetItem.class);
        OperationalActivityBudgetItem saved = service.save(entity);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{idOperationalActivity}/{idBudgetItem}")
            .buildAndExpand(
                saved.getOperationalActivity().getIdOperationalActivity(),
                saved.getBudgetItem().getIdBudgetItem()
            ).toUri();

        return ResponseEntity.created(location).build();
    }

    // Actualizar un registro existente
    @PutMapping
    public ResponseEntity<OperationalActivityBudgetItem> update(@Valid @RequestBody OperationalActivityBudgetItemDTO dto) {
        Integer idOperationalActivity = dto.getOperationalActivity().getIdOperationalActivity();
        Integer idBudgetItem = dto.getBudgetItem().getIdBudgetItem();
        Integer orderItem = dto.getOrderItem();

        OperationalActivityBudgetItem obj = service.findByIds(idOperationalActivity, idBudgetItem, orderItem);
        if (obj == null) {
            throw new ModelNotFoundException("IDs DO NOT EXIST: " + idOperationalActivity + ", " + idBudgetItem + ","+ orderItem);
        }
        // dto.setCreateTime(obj.getCreateTime());
        service.update(mapper.map(dto, OperationalActivityBudgetItem.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Eliminar un registro específico por ambos IDs
    @DeleteMapping("/{idOperationalActivity}/{idBudgetItem}/{orderItem}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer idOperationalActivity,
            @PathVariable Integer idBudgetItem,
            @PathVariable Integer orderItem) {

        OperationalActivityBudgetItem obj = service.findByIds(idOperationalActivity, idBudgetItem, orderItem);

        if (obj == null) {
            throw new ModelNotFoundException("IDs DO NOT EXIST: " + idOperationalActivity + ", " + idBudgetItem + ","+ orderItem);
        }
        service.deleteByIds(idOperationalActivity, idBudgetItem, orderItem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Eliminar todos los registros de una actividad operativa
    @DeleteMapping("/operational-activity/{idOperationalActivity}")
    public ResponseEntity<Void> deleteByOperationalActivity(
            @PathVariable Integer idOperationalActivity) {
        service.deleteByOperationalActivity(idOperationalActivity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}