package com.quantum.auth.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaToken) {
        RestTemplate restTemplate = new RestTemplate();
        @SuppressWarnings("deprecation")
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(RECAPTCHA_VERIFY_URL)
                .queryParam("secret", recaptchaSecret)
                .queryParam("response", recaptchaToken);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(uriBuilder.toUriString(), null, Map.class);

        return response != null && (Boolean) response.get("success");
    }
}