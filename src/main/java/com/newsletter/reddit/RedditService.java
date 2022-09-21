package com.newsletter.reddit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class RedditService {
    private final RedditAuth auth;

    @Autowired
    public RedditService(RedditAuth auth) {
        this.auth = auth;
    }

    public ArrayList<RedditPost> getPostsFromApi(String subredditName, PostType postType) throws JsonProcessingException {
        WebClient client = WebClient.builder().build();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("oauth.reddit.com")
                .path("r/" + subredditName + "/" + postType.toString().toLowerCase())
                .queryParam("limit", 15)
                .build();

                String response = client.get().uri(uriComponents.toUri())
                        .headers(h -> h.setBearerAuth(auth.getToken()))
                        .retrieve()
                        .bodyToMono(String.class).block();
                return get5PostsFromJsonResponse(response);
    }

    private ArrayList<RedditPost> get5PostsFromJsonResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<RedditPost> posts = new ArrayList<>();
        Iterator<JsonNode> it = objectMapper.readTree(response)
                .get("data")
                .get("children")
                .iterator();
        while (posts.size() < 5 && it.hasNext()) {
            JsonNode e = it.next();
            if(e.get("data").get("stickied").asBoolean()) {
                // if post is pinned - ignore
                    continue;
            }
            posts.add(objectMapper.treeToValue(e.get("data"), RedditPost.class));
        }
        return posts;
    }
}
