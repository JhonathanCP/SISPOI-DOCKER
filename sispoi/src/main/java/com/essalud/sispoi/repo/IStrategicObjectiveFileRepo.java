package com.essalud.sispoi.repo;

import com.essalud.sispoi.model.StrategicObjective;
import com.essalud.sispoi.model.StrategicObjectiveFile;



public interface IStrategicObjectiveFileRepo extends _IGenericRepo<StrategicObjectiveFile, Integer>{

    StrategicObjectiveFile findByStrategicObjective(StrategicObjective strategicObjective);

}