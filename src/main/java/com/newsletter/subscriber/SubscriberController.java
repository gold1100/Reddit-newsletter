package com.newsletter.subscriber;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/subscriber")
public class SubscriberController {

    SubscriberService subscriberService;

    @Autowired
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> addSubscriber(@RequestBody Subscriber subscriber){
        return subscriberService.addSubscriber(subscriber);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteSubscriber(@RequestBody Subscriber subscriber){
        return subscriberService.deleteSubscriber(subscriber);
    }
}
