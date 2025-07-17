package com.quantum.auth.service.impl;

import com.quantum.auth.model.Role;
import com.quantum.auth.repo.IRoleRepo;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CRUDImpl<Role, Integer> implements IRoleService{

    @Autowired
    private IRoleRepo repo;

    @Override
    protected IGenericRepo<Role, Integer> getRepo() {
        return repo;
    }

    @Override
    public Role findRoleById(Integer id) {
        return repo.findById(id).orElse(null);
    }

}