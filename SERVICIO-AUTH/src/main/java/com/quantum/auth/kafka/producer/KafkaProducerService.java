// Paso 1: Crear el paquete kafka.producer y la clase KafkaProducerService
package com.quantum.auth.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.quantum.auth.kafka.EmailRequest;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, EmailRequest> kafkaTemplate;

    private final String topic = "email-topic";

    public void sendEmailEvent(EmailRequest emailRequest) {
        kafkaTemplate.send(topic, emailRequest);
    }
    
}