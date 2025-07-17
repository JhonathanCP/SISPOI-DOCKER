package com.quantum.auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Clase S2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    private String username;
    private String password;
    private String recaptchaToken;
    
}
