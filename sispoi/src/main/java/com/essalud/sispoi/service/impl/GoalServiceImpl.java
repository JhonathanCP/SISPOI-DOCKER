package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.Goal;
import com.essalud.sispoi.repo.IGoalRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl extends _CRUDImpl<Goal, Integer> implements IGoalService{

    @Autowired
    private IGoalRepo repo;

    @Override
    protected _IGenericRepo<Goal, Integer> getRepo() {
        return repo;
    }

}