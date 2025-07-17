package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.MeasurementType;
import com.essalud.sispoi.repo.IMeasurementTypeRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IMeasurementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementTypeServiceImpl extends _CRUDImpl<MeasurementType, Integer> implements IMeasurementTypeService{

    @Autowired
    private IMeasurementTypeRepo repo;

    @Override
    protected _IGenericRepo<MeasurementType, Integer> getRepo() {
        return repo;
    }

}