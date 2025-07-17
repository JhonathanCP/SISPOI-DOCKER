package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.BudgetType;
import com.essalud.sispoi.repo.IBudgetTypeRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IBudgetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetTypeServiceImpl extends _CRUDImpl<BudgetType, Integer> implements IBudgetTypeService{

    @Autowired
    private IBudgetTypeRepo repo;

    @Override
    protected _IGenericRepo<BudgetType, Integer> getRepo() {
        return repo;
    }

}