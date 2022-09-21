package com.newsletter.subscriber;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscriberServiceTest {

    @Mock
    SubscriberRepository subscriberRepository;
    SubscriberService underTest;
    AutoCloseable closeable;

    @BeforeEach
    void setUp(){
        closeable = MockitoAnnotations.openMocks(this);
        underTest = new SubscriberService(subscriberRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }



    @Test
    void itShouldAddSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("kowal.kowalski@gmail.com");
        //when
        ResponseEntity<String> tested = underTest.addSubscriber(subscriber);

        //then
        verify(subscriberRepository).save(subscriber);
        assertEquals(tested, ResponseEntity.status(HttpStatus.CREATED).body("Email successfully added to subscription list"));

    }

    @Test
    void itShouldThrowBadRequestException(){
        //given
        Subscriber subscriber0 = new Subscriber("kowalkowalskigmail.com");
        Subscriber subscriber1 = new Subscriber("@gmail.com");
        Subscriber subscriber2 = new Subscriber("kowalkowalski@gmail");
        Subscriber subscriber3 = new Subscriber(".kowal@gmail.com");
        Subscriber subscriber4 = new Subscriber("kowal@gmail.com/");
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(subscriber0);
        subscribers.add(subscriber1);
        subscribers.add(subscriber2);
        subscribers.add(subscriber3);
        subscribers.add(subscriber4);

        // when
        subscribers.forEach(s -> {
            ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> underTest.addSubscriber(s));
            //then
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("400 BAD_REQUEST \"Provide a valid email address\"", thrown.getMessage());

        });
    }
    @Test
    void itShouldThrowConflictException(){
        //given
        Subscriber subscriber = new Subscriber("kowalkowalski@gmail.com");
        when(subscriberRepository.existsSubscriberByEmail(subscriber.getEmail())).thenReturn(true);
        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> underTest.addSubscriber(subscriber));

        // then
        assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
        assertEquals("409 CONFLICT \"This email address is already subscribed\"", thrown.getMessage());

        // then


    }


    @Test
    void itShouldDeleteSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("kowal.kowalski@gmail.com");
        when(subscriberRepository.existsSubscriberByEmail(subscriber.getEmail())).thenReturn(true);
        //when
        ResponseEntity<String> tested = underTest.deleteSubscriber(subscriber);
        //then
        verify(subscriberRepository).delete(subscriber);
        assertEquals(tested, ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully unsubscribed"));
    }

    @Test
    void itShouldThrowNotFoundException(){
        //given
        Subscriber subscriber = new Subscriber("kowal.kowalski@gmail.com");
        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> underTest.deleteSubscriber(subscriber));
        //then
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("404 NOT_FOUND \"This email is not on subscription list\"", thrown.getMessage());
    }


}