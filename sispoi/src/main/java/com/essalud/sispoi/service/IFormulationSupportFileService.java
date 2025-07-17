package com.essalud.sispoi.service;

import com.essalud.sispoi.model.FormulationSupportFile;


public interface IFormulationSupportFileService extends _ICRUD<FormulationSupportFile, Integer> {

    int saveFile(FormulationSupportFile formulationSupportFile);

	byte[] getFile(Integer idFormulationSupportFile);

    FormulationSupportFile findByFormulationId(Integer idFormulation);

}
