package com.quantum.auth.repo;

import com.quantum.auth.model.LoginAudit;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoginAuditRepo extends IGenericRepo<LoginAudit, Integer> {

}