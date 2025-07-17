package com.quantum.auth.service;

import com.quantum.auth.model.User;

public interface ILoginService {

    User checkUsername(String username);
    void changePassword(String password, String username);
    
}
