package com.quantum.auth.service;

import com.quantum.auth.model.Dependency;


public interface IDependencyService extends ICRUD<Dependency, Integer> {

    Dependency findDependencyById(Integer id);
    
}
