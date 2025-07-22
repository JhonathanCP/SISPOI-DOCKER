package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.FormulationType;
import com.essalud.sispoi.repo.IFormulationTypeRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IFormulationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulationTypeServiceImpl extends _CRUDImpl<FormulationType, Integer> implements IFormulationTypeService{

    @Autowired
    private IFormulationTypeRepo repo;

    @Override
    protected _IGenericRepo<FormulationType, Integer> getRepo() {
        return repo;
    }

}