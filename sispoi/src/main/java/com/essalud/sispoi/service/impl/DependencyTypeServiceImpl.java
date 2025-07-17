package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.DependencyType;
import com.essalud.sispoi.repo.IDependencyTypeRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IDependencyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependencyTypeServiceImpl extends _CRUDImpl<DependencyType, Integer> implements IDependencyTypeService{

    @Autowired
    private IDependencyTypeRepo repo;

    @Override
    protected _IGenericRepo<DependencyType, Integer> getRepo() {
        return repo;
    }

}