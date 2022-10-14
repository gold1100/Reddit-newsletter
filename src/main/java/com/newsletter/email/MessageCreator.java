package com.newsletter.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class MessageCreator {

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final JavaMailSender javaMailSender;


    public MessageCreator(SpringTemplateEngine thymeleafTemplateEngine, JavaMailSender javaMailSender) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.javaMailSender = javaMailSender;
    }

    public MimeMessage createMessageFromTemplate(String to,
                                                 String subject,
                                                 String template,
                                                 Map<String, Object> values){
        String body = createHtmlBody(template, values);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;

    }

    private String createHtmlBody(String template, Map<String, Object> variables){
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        return thymeleafTemplateEngine.process(template, thymeleafContext);
    }
}
