package com.essalud.sispoi.service;

import com.essalud.sispoi.dto.MinMaxYearsDTO;
import com.essalud.sispoi.model.StrategicObjective;


public interface IStrategicObjectiveService extends _ICRUD<StrategicObjective, Integer> {

    MinMaxYearsDTO getMinMaxYears();

}
