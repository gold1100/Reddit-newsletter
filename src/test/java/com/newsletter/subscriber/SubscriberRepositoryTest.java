package com.newsletter.subscriber;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class SubscriberRepositoryTest {

    @Autowired
    SubscriberRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByEmail() {
        // given
        Subscriber subscriber = new Subscriber("testemail@gmail.org", LocalDate.now());
        underTest.save(subscriber);

        // when
        Subscriber expected = underTest.findByEmail(subscriber.getEmail());


        // then
        assertEquals(subscriber, expected);
    }

    @Test
    void itShouldNotFindByEmail(){
        // given

        //when
        Subscriber tested = underTest.findByEmail("testemail@gmail.org");

        //then
        assertNull(tested);
    }

    @Test
    void itShouldCheckIfExistsSubscriberByEmail() {
        // given
        Subscriber subscriber = new Subscriber("testemail@gmail.org");
        underTest.save(subscriber);

        //then
        assertTrue(underTest.existsSubscriberByEmail("testemail@gmail.org"));
        assertFalse(underTest.existsSubscriberByEmail("doesntexist@gmail.com"));

    }
}