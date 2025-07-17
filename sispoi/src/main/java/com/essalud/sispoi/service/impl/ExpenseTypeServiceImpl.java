package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ExpenseType;
import com.essalud.sispoi.repo.IExpenseTypeRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IExpenseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseTypeServiceImpl extends _CRUDImpl<ExpenseType, Integer> implements IExpenseTypeService{

    @Autowired
    private IExpenseTypeRepo repo;

    @Override
    protected _IGenericRepo<ExpenseType, Integer> getRepo() {
        return repo;
    }

}