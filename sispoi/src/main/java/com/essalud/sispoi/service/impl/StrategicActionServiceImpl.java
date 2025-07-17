package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.StrategicAction;
import com.essalud.sispoi.repo.IStrategicActionRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IStrategicActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategicActionServiceImpl extends _CRUDImpl<StrategicAction, Integer> implements IStrategicActionService{

    @Autowired
    private IStrategicActionRepo repo;

    @Override
    protected _IGenericRepo<StrategicAction, Integer> getRepo() {
        return repo;
    }

}