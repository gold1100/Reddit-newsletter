package com.newsletter;


import com.newsletter.email.EmailSender;
import com.newsletter.reddit.RedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLR implements CommandLineRunner {

    EmailSender emailSender;
    RedditService redditService;

    @Autowired
    public CommandLR(EmailSender emailSender, RedditService redditService) {
        this.emailSender = emailSender;
        this.redditService = redditService;
    }

    @Override
    public void run(String... args) throws Exception{
        emailSender.sendNewsletters();
    }
}
