package com.essalud.sispoi.repo;

import com.essalud.sispoi.model.StrategicActionFile;
import com.essalud.sispoi.model.StrategicAction;


public interface IStrategicActionFileRepo extends _IGenericRepo<StrategicActionFile, Integer>{

    StrategicActionFile findByStrategicAction(StrategicAction strategicAction);

}