package com.newsletter.reddit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public
class RedditService {

    @Value("${reddit.auth.id}")
    private String clientId;
    @Value("${reddit.auth.secret}")
    private String secret;
    @Value("${reddit.auth.url}")
    private String authUrl;
    private final ObjectMapper objectMapper;

    public RedditService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<RedditPost> getPosts(URI uri, String authToken) {
        String response = httpGetRequest(uri, authToken);
        return extractPostsFromJson(response, 5);
    }

    public String getAuthToken() {
        String response = httpAuthPostRequest((buildAuthUrl()));
        return extractTokenFromJson(response);
    }

    @CacheEvict(value="token", allEntries = true)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 55)
    public void evictCache(){
    }

    private String extractTokenFromJson(String response){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response).findValue("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RedditPost> extractPostsFromJson(String response, int maxPostCount)  {
        try {
            ArrayList<RedditPost> posts = new ArrayList<>();
            Iterator<JsonNode> it = objectMapper.readTree(response)
                    .get("data")
                    .get("children")
                    .iterator();
            while (posts.size() < maxPostCount && it.hasNext()) {
                JsonNode e = it.next();
                if(e.get("data").get("stickied").asBoolean()) {
                    // if post is pinned - ignore
                    continue;
                }
                posts.add(objectMapper.treeToValue(e.get("data"), RedditPost.class));
            }
            return posts;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String httpAuthPostRequest(URI uri){
        WebClient client = WebClient.builder().build();
        return client.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(clientId, secret))
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private String httpGetRequest(URI uri, String token){
        WebClient client = WebClient.builder().build();
        return client.get().uri(uri)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private URI buildAuthUrl(){
        return UriComponentsBuilder
                .fromUriString(authUrl)
                .queryParam("scope", "read")
                .build().toUri();
    }
}
