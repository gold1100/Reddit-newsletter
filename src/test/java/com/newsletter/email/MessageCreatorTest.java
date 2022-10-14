package com.newsletter.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class MessageCreatorTest {

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    MessageCreator underTest;


    @Test
    void itShouldCreateMessage() throws MessagingException, IOException {
        //given
        Map<String, Object> values = Map.of(
                "testValue", "value0",
                "testValue2", "value1"
        );
        File resource = new ClassPathResource("expected-message.txt").getFile();
        String expectedBody = new String(Files.readAllBytes(resource.toPath()));

        //when
        MimeMessage message = underTest.createMessageFromTemplate(
                "testemail@gmail.com",
                "testSubject",
                "test-template.html",
                values
        );

        //then
        assertEquals(message.getAllRecipients()[0].toString(), "testemail@gmail.com");
        assertEquals(message.getSubject(), "testSubject");
        assertEquals(expectedBody, extractEmailBodyFromMessageContent(message.getContent()));

    }

    private String extractEmailBodyFromMessageContent(Object content) throws MessagingException, IOException {
        if(content instanceof String) {
            return (String) content;
        } else if(content instanceof MimeMultipart mp) {
            for(int i = 0; i < mp.getCount(); ++i) {
                BodyPart bodyPart = mp.getBodyPart(i);
                if(bodyPart.getContent() instanceof String) {
                    return (String) bodyPart.getContent();
                } else if(bodyPart.getContent() instanceof MimeMultipart mpContent) {
                    return extractEmailBodyFromMessageContent(mpContent);
                }
                }

            }
        return "";
    }

}