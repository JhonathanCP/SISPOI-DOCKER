package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.ExecutedMonthlyGoal;
import com.essalud.sispoi.model.OperationalActivity;


public interface IExecutedMonthlyGoalRepo extends _IGenericRepo<ExecutedMonthlyGoal, Integer>{

    List<ExecutedMonthlyGoal> findByOperationalActivity(OperationalActivity operationalActivity);

}