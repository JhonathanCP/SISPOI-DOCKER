package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.repo.IDependencyRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependencyServiceImpl extends _CRUDImpl<Dependency, Integer> implements IDependencyService{

    @Autowired
    private IDependencyRepo repo;

    @Override
    protected _IGenericRepo<Dependency, Integer> getRepo() {
        return repo;
    }

}