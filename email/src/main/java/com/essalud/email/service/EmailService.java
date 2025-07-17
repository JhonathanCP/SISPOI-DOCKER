package com.essalud.email.service;

import com.essalud.email.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(EmailRequest request) throws MessagingException {
        Context context = new Context();
        context.setVariables(request.getVariables());

        String htmlContent = templateEngine.process(request.getTemplateName(), context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(htmlContent, true);
        helper.setFrom("gcpp.gpc05@essalud.gob.pe");
        helper.addInline("logo_essalud_white.png", new ClassPathResource("assets/logo_essalud_white.png"));

        mailSender.send(message);
    }
}