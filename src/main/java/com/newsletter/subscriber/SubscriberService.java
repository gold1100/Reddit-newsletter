package com.newsletter.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class SubscriberService {

    SubscriberRepository subscriberRepository;

    @Autowired
    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }


    public ResponseEntity<String> addSubscriber(Subscriber subscriber) {
        if(!isValidEmail(subscriber.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid email address");
        }
        if(isInDatabase(subscriber.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email address is already subscribed");
        }
        subscriber.setJoinDate(LocalDate.now());
        subscriberRepository.save(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email successfully added to subscription list");
    }

    public ResponseEntity<String> deleteSubscriber(Subscriber subscriber) {
        if(!isInDatabase(subscriber.getEmail())){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This email is not on subscription list");
        }

        subscriberRepository.delete(subscriber);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully unsubscribed");
    }




    private boolean isInDatabase(String email) {
       return subscriberRepository.existsSubscriberByEmail(email);
    }

    private boolean isValidEmail(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
