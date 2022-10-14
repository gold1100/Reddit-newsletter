package com.newsletter.subscriber;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;


@Document
public class Subscriber {
    @Id
    private String id;
    private String email;
    @JsonIgnore
    private LocalDate joinDate;

    public Subscriber() {
    }

    public Subscriber(String email) {
        this.email = email;
    }

    public Subscriber(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return Objects.equals(getId(), that.getId()) && getEmail().equals(that.getEmail()) && Objects.equals(getJoinDate(), that.getJoinDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getJoinDate());
    }
}
