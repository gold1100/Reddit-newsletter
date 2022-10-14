package com.newsletter.subscriber;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SubscriberService {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final SubscriberRepository subscriberRepository;


    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public List<Subscriber> getSubscriberList() {
        return subscriberRepository.findAll();
    }

    public Subscriber addSubscriber(Subscriber subscriber) {
        if(!isValidEmail(subscriber.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid email address");
    }
        if(subscriberRepository.existsSubscriberByEmail(subscriber.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email address is already subscribed");
        }
        subscriber.setJoinDate(LocalDate.now());
        subscriberRepository.save(subscriber);
        return subscriber;
    }

    public void deleteSubscriber(String id) {
        if(!subscriberRepository.existsById(id)){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This email is not on subscription list");
        }
        subscriberRepository.deleteById(id);
    }

    private boolean isValidEmail(String email){
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }


}
