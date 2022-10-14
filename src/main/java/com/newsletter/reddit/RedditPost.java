package com.newsletter.reddit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RedditPost {

    private String title;
    @JsonProperty("selftext")
    private String text;
    private String url;


    public RedditPost(String title, String text, String url) {
        this.title = title;
        this.text = text;
        this.url = url;
    }

    public RedditPost() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RedditPost{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RedditPost that)) return false;
        return getTitle().equals(that.getTitle()) && getText().equals(that.getText()) && getUrl().equals(that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getText(), getUrl());
    }


}



