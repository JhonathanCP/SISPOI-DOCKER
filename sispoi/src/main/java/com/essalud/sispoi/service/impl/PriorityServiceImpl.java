package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.Priority;
import com.essalud.sispoi.repo.IPriorityRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriorityServiceImpl extends _CRUDImpl<Priority, Integer> implements IPriorityService{

    @Autowired
    private IPriorityRepo repo;

    @Override
    protected _IGenericRepo<Priority, Integer> getRepo() {
        return repo;
    }

}