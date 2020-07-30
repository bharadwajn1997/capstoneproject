package com.cognizant.capstoneprojectone.models;

import java.util.Objects;

public class Feedback {

    private String id;
    private String name;
    private String feedback;
    private String email;
    private Rating rating;
    public enum Rating {Satisfied, Average, Poor}

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "name='" + name + '\'' +
                ", feedback='" + feedback + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback1 = (Feedback) o;
        return id == feedback1.id &&
                Objects.equals(name, feedback1.name) &&
                Objects.equals(feedback, feedback1.feedback) &&
                Objects.equals(email, feedback1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, feedback, email);
    }

}
