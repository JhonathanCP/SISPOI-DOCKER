package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.CostCenter;
import com.essalud.sispoi.repo.ICostCenterRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.ICostCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostCenterServiceImpl extends _CRUDImpl<CostCenter, Integer> implements ICostCenterService{

    @Autowired
    private ICostCenterRepo repo;

    @Override
    protected _IGenericRepo<CostCenter, Integer> getRepo() {
        return repo;
    }

}