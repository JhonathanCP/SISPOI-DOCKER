package com.essalud.sispoi.repo;

import java.util.List;

import com.essalud.sispoi.model.CostCenter;
import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.OperationalActivity;


public interface IOperationalActivityRepo extends _IGenericRepo<OperationalActivity, Integer>{

    List<OperationalActivity> findByFormulation(Formulation formulation);

    // NEW Method: Find all operational activities by CostCenter
    List<OperationalActivity> findByCostCenter(CostCenter costCenter);

}