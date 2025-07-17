package com.essalud.sispoi.service;

import com.essalud.sispoi.model.StrategicObjectiveFile;


public interface IStrategicObjectiveFileService extends _ICRUD<StrategicObjectiveFile, Integer> {

    int saveFile(StrategicObjectiveFile strategicObjectiveFile);

	byte[] getFile(Integer idStrategicObjectiveFile);

    StrategicObjectiveFile findByStrategicObjectiveId(Integer idStrategicObjective);

}
