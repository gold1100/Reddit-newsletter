package com.newsletter.subscriber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
class SubscriberControllerTest {

    @Mock
    SubscriberService subscriberService;
    @InjectMocks
    SubscriberController underTest;

    @BeforeEach
     void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldCallServiceGetSubscribers(){
        //when
        underTest.getSubscriberList();

        //then
        verify(subscriberService).getSubscriberList();
    }

    @Test
    void itShouldCallServiceAddSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("jjj@ggg.com");

        // when
        Subscriber test = underTest.addSubscriber(subscriber);
        Subscriber expected = verify(subscriberService).addSubscriber(subscriber);

        // then
        assertEquals(test, expected);
    }

    @Test
    void itShouldCallServiceDeleteSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("testId" ,"jjj@ggg.com");

        // when
        underTest.deleteSubscriber("testId");

        // then
        verify(subscriberService).deleteSubscriber("testId");
    }
}