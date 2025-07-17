package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.model.Formulation;


public interface IFormulationRepo extends _IGenericRepo<Formulation, Integer>{

    List<Formulation> findByDependencyAndYear(Dependency dependency, Integer year);

}