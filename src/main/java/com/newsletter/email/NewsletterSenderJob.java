package com.newsletter.email;


import com.newsletter.reddit.PostType;
import com.newsletter.reddit.RedditClient;
import com.newsletter.reddit.RedditPost;
import com.newsletter.subscriber.SubscriberRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@EnableScheduling
public class NewsletterSenderJob {

    private static final int MAX_POST_LENGTH = 220;
    private final RedditClient redditClient;
    private final MessageCreator messageCreator;
    private final JavaMailSender emailSender;
    private final SubscriberRepository subscriberRepository;

    public NewsletterSenderJob(RedditClient redditClient, MessageCreator messageCreator, JavaMailSender emailSender, SubscriberRepository subscriberRepository) {
        this.redditClient = redditClient;
        this.messageCreator = messageCreator;
        this.emailSender = emailSender;
        this.subscriberRepository = subscriberRepository;
    }


    @Scheduled(cron = "* * 8 * * *")
    public void sendNewsletters(){
        sendRedditNewsletter("learnjava", PostType.HOT);
    }

    private void sendRedditNewsletter(String subredditName, PostType postType){
        List<RedditPost> posts = redditClient.getPosts(subredditName, postType);
        formatPosts(posts);

        subscriberRepository.findAll().forEach(subscriber -> {
            MimeMessage message = messageCreator.createMessageFromTemplate(
                    subscriber.getEmail(),
                    "Daily reddit newsletter - " + LocalDate.now(),
                    "newsletter-template.html",
                    Collections.singletonMap("posts", posts));
            emailSender.send(message);
        });
        }


    private void formatPosts(List<RedditPost> posts) {
        posts.forEach(post -> {
            if(post.getText().length() > MAX_POST_LENGTH){
                String shortText = post.getText().substring(0,MAX_POST_LENGTH);
                post.setText(shortText.substring(0, shortText.lastIndexOf(" ")) +"[...]");
            }
        });
    }
}
