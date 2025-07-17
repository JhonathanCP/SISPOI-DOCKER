package com.essalud.sispoi.service;

import java.util.List;

import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.OperationalActivity;


public interface IOperationalActivityService extends _ICRUD<OperationalActivity, Integer> {

    List<OperationalActivity> findByFormulation(Formulation formulation);

    String getHigherCorrelativeCodeByCostCenter(Integer idCostCenter);

}
