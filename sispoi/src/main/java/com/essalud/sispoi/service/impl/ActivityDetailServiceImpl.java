package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.ActivityDetail;
import com.essalud.sispoi.repo.IActivityDetailRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IActivityDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityDetailServiceImpl extends _CRUDImpl<ActivityDetail, Integer> implements IActivityDetailService{

    @Autowired
    private IActivityDetailRepo repo;

    @Override
    protected _IGenericRepo<ActivityDetail, Integer> getRepo() {
        return repo;
    }

}