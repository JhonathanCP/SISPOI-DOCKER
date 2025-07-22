package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.MonthlyGoal;
import com.essalud.sispoi.model.OperationalActivity;


public interface IMonthlyGoalRepo extends _IGenericRepo<MonthlyGoal, Integer>{

    List<MonthlyGoal> findByOperationalActivity(OperationalActivity operationalActivity);

}