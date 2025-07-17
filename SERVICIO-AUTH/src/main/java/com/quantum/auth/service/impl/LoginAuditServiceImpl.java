package com.quantum.auth.service.impl;

import com.quantum.auth.model.LoginAudit;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.repo.ILoginAuditRepo;
import com.quantum.auth.service.ILoginAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAuditServiceImpl extends CRUDImpl<LoginAudit, Integer> implements ILoginAuditService{

    @Autowired
    private ILoginAuditRepo repo;

    @Override
    protected IGenericRepo<LoginAudit, Integer> getRepo() {
        return repo;
    }

}
