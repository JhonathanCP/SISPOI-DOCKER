package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.Goal;
import com.essalud.sispoi.model.OperationalActivity;


public interface IGoalRepo extends _IGenericRepo<Goal, Integer>{

    List<Goal> findByOperationalActivity(OperationalActivity operationalActivity);

}