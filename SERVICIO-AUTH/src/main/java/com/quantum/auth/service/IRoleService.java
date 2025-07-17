package com.quantum.auth.service;

import com.quantum.auth.model.Role;


public interface IRoleService extends ICRUD<Role, Integer> {

    Role findRoleById(Integer id);
    
}
