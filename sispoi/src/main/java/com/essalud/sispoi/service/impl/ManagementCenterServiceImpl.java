package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ManagementCenter;
import com.essalud.sispoi.repo.IManagementCenterRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IManagementCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagementCenterServiceImpl extends _CRUDImpl<ManagementCenter, Integer> implements IManagementCenterService{

    @Autowired
    private IManagementCenterRepo repo;

    @Override
    protected _IGenericRepo<ManagementCenter, Integer> getRepo() {
        return repo;
    }

}