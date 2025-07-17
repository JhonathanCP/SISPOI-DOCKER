package com.essalud.sispoi.service;

import com.essalud.sispoi.model.StrategicActionFile;


public interface IStrategicActionFileService extends _ICRUD<StrategicActionFile, Integer> {

    int saveFile(StrategicActionFile strategicActionFile);

	byte[] getFile(Integer idStrategicActionFile);

    StrategicActionFile findByStrategicActionId(Integer idStrategicAction);

}
