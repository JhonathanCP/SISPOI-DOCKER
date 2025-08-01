package com.essalud.sispoi.service;

import java.util.List;

import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.model.Formulation;


public interface IFormulationService extends _ICRUD<Formulation, Integer> {


    List<Formulation> findByDependencyAndYear(Dependency dependency, Integer year);

    Formulation addModification(Integer originalFormulationId, Integer newQuarter);

    Formulation addModificationPe(Integer originalFormulationId, Integer newMonth);

}
