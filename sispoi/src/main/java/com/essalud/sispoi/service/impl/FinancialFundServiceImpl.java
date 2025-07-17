package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.FinancialFund;
import com.essalud.sispoi.repo.IFinancialFundRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IFinancialFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialFundServiceImpl extends _CRUDImpl<FinancialFund, Integer> implements IFinancialFundService{

    @Autowired
    private IFinancialFundRepo repo;

    @Override
    protected _IGenericRepo<FinancialFund, Integer> getRepo() {
        return repo;
    }

}