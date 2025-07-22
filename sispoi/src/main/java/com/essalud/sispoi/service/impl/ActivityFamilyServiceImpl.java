package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ActivityFamily;
import com.essalud.sispoi.repo.IActivityFamilyRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IActivityFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityFamilyServiceImpl extends _CRUDImpl<ActivityFamily, Integer> implements IActivityFamilyService{

    @Autowired
    private IActivityFamilyRepo repo;

    @Override
    protected _IGenericRepo<ActivityFamily, Integer> getRepo() {
        return repo;
    }

}