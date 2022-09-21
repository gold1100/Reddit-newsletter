package com.newsletter.subscriber;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


class SubscriberControllerTest {

    @Mock
    SubscriberService subscriberService;
    SubscriberController underTest;
    AutoCloseable closeable;

    @BeforeEach
     void setUp(){
        closeable = MockitoAnnotations.openMocks(this);
        underTest = new SubscriberController(subscriberService);
    }


    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void itShouldCallServiceAddSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("jjj@ggg.com");
        // when
        ResponseEntity<?> test = underTest.addSubscriber(subscriber);
        ResponseEntity<?> expected = verify(subscriberService).addSubscriber(subscriber);

        // then
        assertEquals(test, expected);
    }

    @Test
    void itShouldCallServiceDeleteSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("jjj@ggg.com");
        // when
        ResponseEntity<?> test = underTest.deleteSubscriber(subscriber);
        ResponseEntity<?> expected = verify(subscriberService).deleteSubscriber(subscriber);

        // then
        assertEquals(test, expected);
    }
}