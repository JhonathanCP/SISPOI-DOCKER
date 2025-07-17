package com.quantum.auth.service.impl;

import com.quantum.auth.model.Dependency;
import com.quantum.auth.repo.IDependencyRepo;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.service.IDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependencyServiceImpl extends CRUDImpl<Dependency, Integer> implements IDependencyService{

    @Autowired
    private IDependencyRepo repo;

    @Override
    protected IGenericRepo<Dependency, Integer> getRepo() {
        return repo;
    }

    @Override
    public Dependency findDependencyById(Integer id) {
        return repo.findById(id).orElse(null);
    }

}