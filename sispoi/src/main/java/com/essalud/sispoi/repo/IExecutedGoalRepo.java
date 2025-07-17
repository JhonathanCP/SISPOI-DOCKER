package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.ExecutedGoal;
import com.essalud.sispoi.model.OperationalActivity;


public interface IExecutedGoalRepo extends _IGenericRepo<ExecutedGoal, Integer>{

    List<ExecutedGoal> findByOperationalActivity(OperationalActivity operationalActivity);

}