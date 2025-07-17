package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.dto.MinMaxYearsDTO;
import com.essalud.sispoi.model.StrategicObjective;
import com.essalud.sispoi.repo.IStrategicObjectiveRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IStrategicObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StrategicObjectiveServiceImpl extends _CRUDImpl<StrategicObjective, Integer> implements IStrategicObjectiveService{

    @Autowired
    private IStrategicObjectiveRepo repo;

    @Override
    protected _IGenericRepo<StrategicObjective, Integer> getRepo() {
        return repo;
    }

    @Override
    @Transactional(readOnly = true) // This is a read-only operation
    public MinMaxYearsDTO getMinMaxYears() {
        Integer minStartYear = repo.findMinStartYear();
        Integer maxEndYear = repo.findMaxEndYear();

        // Handle cases where no strategic objectives exist (min/max queries might return null)
        // You might want to define default values or throw an exception if no data is found
        if (minStartYear == null) {
            // Option 1: Return a DTO with nulls or specific default values indicating no data
            return new MinMaxYearsDTO(null, null);
            // Option 2: Throw a custom exception if it's an error condition for your application
            // throw new DataNotFoundException("No active strategic objectives found to determine min/max years.");
        }

        return new MinMaxYearsDTO(minStartYear, maxEndYear);
    }

}