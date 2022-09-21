package com.newsletter.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newsletter.reddit.RedditService;
import com.newsletter.reddit.PostType;
import com.newsletter.reddit.RedditPost;
import com.newsletter.subscriber.Subscriber;
import com.newsletter.subscriber.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class EmailSender {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    private final RedditService redditService;
    private final SubscriberRepository subscriberRepository;



    @Autowired
    public EmailSender(JavaMailSender emailSender, RedditService redditService, SpringTemplateEngine thymeleafTemplateEngine, SubscriberRepository subscriberRepository) {
        this.emailSender = emailSender;
        this.redditService = redditService;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.subscriberRepository = subscriberRepository;
    }

    @Scheduled(cron = "* * 8 * * *")
    public void sendNewsletters() throws JsonProcessingException, MessagingException {
        ArrayList<RedditPost> posts = redditService.getPostsFromApi("learnjava", PostType.HOT);
        posts.forEach(p -> {
            if(p.getText().length() > 220){
                String shortText = p.getText().substring(0,220);
                p.setText(shortText.substring(0, shortText.lastIndexOf(" ")) +"[...]");
            }
        });

        HashMap<String, Object> messageValues = new HashMap<>();
        messageValues.put("posts", posts);
        for (Subscriber s : subscriberRepository.findAll()) {
            sendMessageUsingThymeleaf(s.getEmail()
                    , "Daily reddit newsletter - " + LocalDate.now()
                    , messageValues);
        }
    }

    private void sendMessageUsingThymeleaf(String to, String subject, Map<String, Object> variables)throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        String htmlBody = thymeleafTemplateEngine.process("newsletter-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }


}


