package com.quantum.auth.service;

import com.quantum.auth.model.User;

public interface IUserService extends ICRUD<User, Integer>{

    User findOneByUsername(String username);

    void addRole(Integer userId, Integer roleId);
    void removeRole(Integer userId, Integer roleId);
    void addDependency(Integer userId, Integer dependencyId);
    void removeDependency(Integer userId, Integer dependencyId);

}
