package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.FormulationState;
import com.essalud.sispoi.repo.IFormulationStateRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IFormulationStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulationStateServiceImpl extends _CRUDImpl<FormulationState, Integer> implements IFormulationStateService{

    @Autowired
    private IFormulationStateRepo repo;

    @Override
    protected _IGenericRepo<FormulationState, Integer> getRepo() {
        return repo;
    }

}