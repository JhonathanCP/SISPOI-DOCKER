package com.essalud.sispoi.repo;

import org.springframework.data.jpa.repository.Query;

import com.essalud.sispoi.model.StrategicObjective;


public interface IStrategicObjectiveRepo extends _IGenericRepo<StrategicObjective, Integer>{

    @Query("SELECT MIN(so.startYear) FROM StrategicObjective so WHERE so.active = true")
    Integer findMinStartYear();

    @Query("SELECT MAX(so.endYear) FROM StrategicObjective so WHERE so.active = true")
    Integer findMaxEndYear();

}