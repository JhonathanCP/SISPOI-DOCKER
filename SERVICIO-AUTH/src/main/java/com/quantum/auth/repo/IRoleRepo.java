package com.quantum.auth.repo;

import com.quantum.auth.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepo extends IGenericRepo<Role, Integer> {
    Role findByName(String name);
}