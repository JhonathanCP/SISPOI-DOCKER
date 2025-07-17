package com.quantum.auth.kafka;

import lombok.Data;

import java.util.Map;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> variables;
}
