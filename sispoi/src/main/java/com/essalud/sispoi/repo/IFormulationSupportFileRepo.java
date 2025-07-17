package com.essalud.sispoi.repo;

import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.FormulationSupportFile;


public interface IFormulationSupportFileRepo extends _IGenericRepo<FormulationSupportFile, Integer>{

    FormulationSupportFile findByFormulation(Formulation formulation);

}