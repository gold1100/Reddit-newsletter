package com.newsletter.email;

import com.newsletter.reddit.RedditClient;
import com.newsletter.reddit.RedditPost;
import com.newsletter.subscriber.SubscriberRepository;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class NewsletterSenderJobTest {

    @Mock
    RedditClient redditClient;
    @Mock
    MessageCreator messageCreator;
    @Mock
    JavaMailSender emailSender;
    @Mock
    SubscriberRepository subscriberRepository;
    @InjectMocks
    NewsletterSenderJob underTest;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldCorrectlyFormatPostsBody(){
        // given
        String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id iaculis risus. In scelerisque leo id mi sodales lobortis. Sed id condimentum dolor. Maecenas sodales viverra lorem a consectetur. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam hendrerit varius nisi et eleifend. Suspendisse at ante leo. Etiam sollicitudin, velit commodo tincidunt laoreet, metus tortor elementum tellus, sit amet porta lorem nisl bibendum risus. Donec eget leo velit. Nunc at tempor nisi";
        List<RedditPost> posts = Lists.newArrayList();
        posts.add(new RedditPost("title", "shortText", "url"));
        posts.add(new RedditPost("title", longText, "url"));

        // when
        when(redditClient.getPosts(any(), any())).thenReturn(posts);
        underTest.sendNewsletters();

        // then
        assertEquals("shortText", posts.get(0).getText());
        assertTrue(posts.get(1).getText().length() < 225);
    }
}