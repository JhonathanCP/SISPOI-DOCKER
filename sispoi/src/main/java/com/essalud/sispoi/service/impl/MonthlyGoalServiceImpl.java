package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.MonthlyGoal;
import com.essalud.sispoi.repo.IMonthlyGoalRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IMonthlyGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyGoalServiceImpl extends _CRUDImpl<MonthlyGoal, Integer> implements IMonthlyGoalService{

    @Autowired
    private IMonthlyGoalRepo repo;

    @Override
    protected _IGenericRepo<MonthlyGoal, Integer> getRepo() {
        return repo;
    }

}