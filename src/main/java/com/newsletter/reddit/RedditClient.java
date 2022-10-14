package com.newsletter.reddit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class RedditClient {

    private final RedditService redditService;

    public RedditClient(RedditService redditService) {
        this.redditService = redditService;
    }

    public List<RedditPost> getPosts(String subredditName, PostType postType){
        URI uri = buildUrl(subredditName, postType);
        return redditService.getPosts(uri, getAuthToken());
    }

    @Cacheable("token")
    public String getAuthToken(){
        return redditService.getAuthToken();
    }

    private URI buildUrl(String subredditName, PostType postType){
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("oauth.reddit.com")
                .path("r/" + subredditName + "/" + postType.toString().toLowerCase())
                .queryParam("limit", 15)
                .build().toUri();
    }

}
