package com.newsletter.subscriber;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends MongoRepository<Subscriber, String> {

     Subscriber findByEmail(String email);
     boolean existsSubscriberByEmail(String email);


}
