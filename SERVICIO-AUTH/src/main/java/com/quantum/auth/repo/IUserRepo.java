package com.quantum.auth.repo;

import com.quantum.auth.model.User;


public interface IUserRepo extends IGenericRepo<User, Integer>{

    //@Query("FROM User u WHERE u.username = :username")
    //DerivedQuery
    User findOneByUsername(String username);

}