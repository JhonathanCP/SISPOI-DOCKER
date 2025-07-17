package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.BudgetItem;
import com.essalud.sispoi.repo.IBudgetItemRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IBudgetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetItemServiceImpl extends _CRUDImpl<BudgetItem, Integer> implements IBudgetItemService{

    @Autowired
    private IBudgetItemRepo repo;

    @Override
    protected _IGenericRepo<BudgetItem, Integer> getRepo() {
        return repo;
    }

}