package com.newsletter.email;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.newsletter.reddit.RedditClient;
import com.newsletter.reddit.RedditPost;
import com.newsletter.subscriber.Subscriber;
import com.newsletter.subscriber.SubscriberRepository;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NewsletterSenderSendingTest {

    @MockBean
    RedditClient redditClient;
    @Autowired
    MessageCreator messageCreator;
    @Autowired
    JavaMailSender emailSender;
    @MockBean
    SubscriberRepository subscriberRepository;
    @Autowired
    NewsletterSenderJob underTest;
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP.withPort(3500))
            .withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

    @BeforeEach
    void setUp(){
        greenMail.start();
    }

    @AfterEach
    void tearDown(){
        greenMail.stop();
    }


    @Test
    void itShouldDeliverEmailsToAllSubscribers(){
        //given
        List<Subscriber> subscribers = Lists.newArrayList();
        subscribers.add(new Subscriber("testemail@gmail.com"));
        subscribers.add(new Subscriber("testemail2@gmail.com"));
        List<RedditPost> posts = Lists.newArrayList();
        posts.add(new RedditPost("title1", "test1", "url1"));
        posts.add(new RedditPost("title2", "test2", "url2"));

        //when
        when(subscriberRepository.findAll()).thenReturn(subscribers);
        when(redditClient.getPosts(any(), any())).thenReturn(posts);
        underTest.sendNewsletters();

        //then
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(2, messages.length);
    }


}
