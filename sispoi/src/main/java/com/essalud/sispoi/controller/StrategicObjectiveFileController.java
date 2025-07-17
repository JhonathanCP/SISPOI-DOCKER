package com.essalud.sispoi.controller;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.essalud.sispoi.dto.StrategicObjectiveFileDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.StrategicObjective;
import com.essalud.sispoi.model.StrategicObjectiveFile;
import com.essalud.sispoi.service.IStrategicObjectiveFileService;
import com.essalud.sispoi.service.IStrategicObjectiveService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/strategic-objective-file")
public class StrategicObjectiveFileController {

	@Autowired
    private ModelMapper mapper;

    @Autowired
    private IStrategicObjectiveFileService service;

    @Autowired
    private IStrategicObjectiveService strategicObjectiveService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Integer> saveStrategicObjectiveFile(@RequestParam("file") MultipartFile file, @RequestParam("idStrategicObjective") Integer idStrategicObjective) throws Exception{

		StrategicObjective strategicObjective = strategicObjectiveService.findById(idStrategicObjective);
        if (strategicObjective == null) {
            throw new ModelNotFoundException("StrategicObjective ID DOES NOT EXIST: " + idStrategicObjective);
        }

		StrategicObjectiveFile ar = new StrategicObjectiveFile();
		ar.setFileExtension(file.getContentType());
		ar.setName(file.getOriginalFilename());
		ar.setFile(file.getBytes());
        ar.setActive(true);
        ar.setStrategicObjective(strategicObjective);

		Integer rpta = service.saveFile(ar);

		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateStrategicObjectiveFile(
                @RequestParam("idStrategicObjective") Integer idStrategicObjective,
                @RequestParam("file") MultipartFile file
            ) throws Exception {

        StrategicObjective strategicObjective = strategicObjectiveService.findById(idStrategicObjective);
        if (strategicObjective == null) {
            throw new ModelNotFoundException("StrategicObjective ID DOES NOT EXIST: " + idStrategicObjective);
        }

        // Busca el archivo asociado al StrategicObjective (relación 1 a 1)
        StrategicObjectiveFile existingFile = service.findByStrategicObjectiveId(idStrategicObjective);
        if (existingFile == null) {
            throw new ModelNotFoundException("StrategicObjectiveFile for StrategicObjective ID DOES NOT EXIST: " + idStrategicObjective);
        }

        existingFile.setFileExtension(file.getContentType());
        existingFile.setName(file.getOriginalFilename());
        existingFile.setFile(file.getBytes());
        // Puedes actualizar otros campos si lo necesitas

        service.saveFile(existingFile);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	@GetMapping("/{idStrategicObjectiveFile}")
    public ResponseEntity<StrategicObjectiveFileDTO> findById(@PathVariable Integer idStrategicObjectiveFile) {
        StrategicObjectiveFileDTO dtoResponse;
        StrategicObjectiveFile obj = service.findById(idStrategicObjectiveFile);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + idStrategicObjectiveFile);
        } else {
            dtoResponse = mapper.map(obj, StrategicObjectiveFileDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

	@GetMapping(value = "/{idStrategicObjectiveFile}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> getStrategicObjectiveFile(@PathVariable("idStrategicObjectiveFile") Integer idStrategicObjectiveFile) throws IOException {

		byte[] arr = service.getFile(idStrategicObjectiveFile);

		return new ResponseEntity<>(arr, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        StrategicObjectiveFile obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}