package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ExecutedGoal;
import com.essalud.sispoi.repo.IExecutedGoalRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IExecutedGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutedGoalImpl extends _CRUDImpl<ExecutedGoal, Integer> implements IExecutedGoalService{

    @Autowired
    private IExecutedGoalRepo repo;

    @Override
    protected _IGenericRepo<ExecutedGoal, Integer> getRepo() {
        return repo;
    }

}