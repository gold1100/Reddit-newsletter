package com.newsletter.subscriber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscriberServiceTest {

    @Mock
    SubscriberRepository subscriberRepository;
    @InjectMocks
    SubscriberService underTest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itShouldGetAllSubscribers(){
        //when
        underTest.getSubscriberList();
        //then
        verify(subscriberRepository).findAll();
    }

    @Test
    void itShouldAddSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("kowal.kowalski@gmail.com");
        //when
        Subscriber tested = underTest.addSubscriber(subscriber);
        //then
        verify(subscriberRepository).save(subscriber);

    }

    @Test
    void itShouldThrowBadRequestExceptionWhenIncorrectEmail(){
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
    void itShouldThrowConflictExceptionWhenSubscriberExistsInDbAlready(){
        //given
        Subscriber subscriber = new Subscriber("kowalkowalski@gmail.com");

        //when
        when(subscriberRepository.existsSubscriberByEmail(subscriber.getEmail())).thenReturn(true);
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> underTest.addSubscriber(subscriber));

        // then
        assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
        assertEquals("409 CONFLICT \"This email address is already subscribed\"", thrown.getMessage());

        // then


    }

    @Test
    void itShouldDeleteSubscriber(){
        //given
        Subscriber subscriber = new Subscriber("testId","emailTest@gmail.com");

        //when
        when(subscriberRepository.existsById(subscriber.getId())).thenReturn(true);
        underTest.deleteSubscriber("testId");
        //then
        verify(subscriberRepository).deleteById("testId");
    }

    @Test
    void itShouldThrowNotFoundExceptionWhenSubscriberIsntInDb(){
        //given
        Subscriber subscriber = new Subscriber("testId" ,"kowal.kowalski@gmail.com");
        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> underTest.deleteSubscriber("testId"));
        //then
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("404 NOT_FOUND \"This email is not on subscription list\"", thrown.getMessage());
    }


}