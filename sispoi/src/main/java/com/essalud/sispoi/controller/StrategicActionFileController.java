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

import com.essalud.sispoi.dto.StrategicActionFileDTO;
import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.StrategicAction;
import com.essalud.sispoi.model.StrategicActionFile;
import com.essalud.sispoi.service.IStrategicActionFileService;
import com.essalud.sispoi.service.IStrategicActionService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/strategic-action-file")
public class StrategicActionFileController {

	@Autowired
    private ModelMapper mapper;

    @Autowired
    private IStrategicActionFileService service;

    @Autowired
    private IStrategicActionService strategicActionService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Integer> saveStrategicActionFile(@RequestParam("file") MultipartFile file) throws Exception{

		int rpta = 0;

		StrategicActionFile ar = new StrategicActionFile();
		ar.setFileExtension(file.getContentType());
		ar.setName(file.getOriginalFilename());
		ar.setFile(file.getBytes());
        ar.setActive(true);

		rpta = service.saveFile(ar);

		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateStrategicActionFile(
                @RequestParam("idStrategicAction") Integer idStrategicAction,
                @RequestParam("file") MultipartFile file
            ) throws Exception {

        StrategicAction strategicAction = strategicActionService.findById(idStrategicAction);
        if (strategicAction == null) {
            throw new ModelNotFoundException("StrategicAction ID DOES NOT EXIST: " + idStrategicAction);
        }

        // Busca el archivo asociado al StrategicAction (relación 1 a 1)
        StrategicActionFile existingFile = service.findByStrategicActionId(idStrategicAction);
        if (existingFile == null) {
            throw new ModelNotFoundException("StrategicActionFile for StrategicAction ID DOES NOT EXIST: " + idStrategicAction);
        }

        existingFile.setFileExtension(file.getContentType());
        existingFile.setName(file.getOriginalFilename());
        existingFile.setFile(file.getBytes());
        // Puedes actualizar otros campos si lo necesitas

        service.saveFile(existingFile);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	@GetMapping("/{idStrategicActionFile}")
    public ResponseEntity<StrategicActionFileDTO> findById(@PathVariable Integer idStrategicActionFile) {
        StrategicActionFileDTO dtoResponse;
        StrategicActionFile obj = service.findById(idStrategicActionFile);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + idStrategicActionFile);
        } else {
            dtoResponse = mapper.map(obj, StrategicActionFileDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

	@GetMapping(value = "/{idStrategicActionFile}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> getStrategicActionFile(@PathVariable("idStrategicActionFile") Integer idStrategicActionFile) throws IOException {

		byte[] arr = service.getFile(idStrategicActionFile);

		return new ResponseEntity<>(arr, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        StrategicActionFile obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}