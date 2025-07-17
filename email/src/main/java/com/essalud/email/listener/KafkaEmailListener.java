package com.essalud.email.listener;

import com.essalud.email.model.EmailRequest;
import com.essalud.email.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailListener {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            EmailRequest request = mapper.readValue(record.value(), EmailRequest.class);
            emailService.sendEmail(request);
        } catch (Exception e) {
            e.printStackTrace(); // En producción, usar logger
        }
    }
}
