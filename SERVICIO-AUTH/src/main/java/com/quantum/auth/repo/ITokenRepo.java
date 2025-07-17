package com.quantum.auth.repo;

import java.util.List;
import java.util.Optional;

import com.quantum.auth.model.Token;

public interface ITokenRepo extends IGenericRepo<Token, Integer>{

    Optional<Token> findByToken(String token);

    void deleteByToken(String token);

    List<Token> findByUsernameAndIsValidTrue(String username);  // Obtener tokens válidos de un usuario

}
