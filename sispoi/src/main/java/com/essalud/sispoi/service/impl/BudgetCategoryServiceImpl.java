package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.BudgetCategory;
import com.essalud.sispoi.repo.IBudgetCategoryRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IBudgetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetCategoryServiceImpl extends _CRUDImpl<BudgetCategory, Integer> implements IBudgetCategoryService{

    @Autowired
    private IBudgetCategoryRepo repo;

    @Override
    protected _IGenericRepo<BudgetCategory, Integer> getRepo() {
        return repo;
    }

}