package com.newsletter.reddit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

@Component
@EnableScheduling
@PropertySource("classpath:application.properties")
public class RedditAuth {

    @Value("${reddit.auth.id}")
    private String clientId;
    @Value("${reddit.auth.secret}")
    private String secret;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 55)
    public void obtainNewToken() throws JsonProcessingException {
        WebClient client = WebClient.builder().build();
        String url = "https://www.reddit.com/api/v1/access_token";
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("scope", "read")
                .build();
        String authResponse = client.post()
                .uri(uriComponents.toUri())
                .headers(httpHeaders -> httpHeaders.setBasicAuth(clientId, secret))
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(String.class).block();
        String token = getTokenFromJson(authResponse);

        // save token
        Preferences.userNodeForPackage(this.getClass()).put("authToken", token);
    }

    public String getToken(){
        return Preferences.userNodeForPackage(this.getClass()).get("authToken", "invalid");
        }

    private String getTokenFromJson(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response).findValue("access_token").asText();
    }
}