package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ExecutedMonthlyGoal;
import com.essalud.sispoi.repo.IExecutedMonthlyGoalRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IExecutedMonthlyGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutedMonthlyGoalImpl extends _CRUDImpl<ExecutedMonthlyGoal, Integer> implements IExecutedMonthlyGoalService{

    @Autowired
    private IExecutedMonthlyGoalRepo repo;

    @Override
    protected _IGenericRepo<ExecutedMonthlyGoal, Integer> getRepo() {
        return repo;
    }

}