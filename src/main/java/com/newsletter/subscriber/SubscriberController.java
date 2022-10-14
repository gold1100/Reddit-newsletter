package com.newsletter.subscriber;


import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/subscriber")
public class SubscriberController {

    private final SubscriberService subscriberService;


    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping
    public List<Subscriber> getSubscriberList(){
        return subscriberService.getSubscriberList();
    }

    @PostMapping
    public Subscriber addSubscriber(@RequestBody Subscriber subscriber){
        return subscriberService.addSubscriber(subscriber);
    }

    @DeleteMapping(path="/{id}")
    public void deleteSubscriber(@PathVariable String id){
        subscriberService.deleteSubscriber(id);
    }
}
