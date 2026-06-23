package com.prashik.scorer.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Player implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Player(){}

    public Player(String firstName, String lastName, String email, String phoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toJson() {
        return String.format("{\"id\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", "
                        + "\"email\": \"%s\", \"phoneNumber\": \"%s\"}"
                , id, firstName, lastName, email, phoneNumber);
    }

    public boolean isEqual(Player player2) {
        return Objects.equals(this.id, player2.getId())
                && Objects.equals(this.firstName, player2.getFirstName())
                && Objects.equals(this.lastName, player2.getLastName())
                && Objects.equals(this.email, player2.getEmail())
                && Objects.equals(this.phoneNumber, player2.getPhoneNumber());
    }

    public boolean isPlayerSame(Player player2) {
        String name1 = this.getFirstName() + " " + this.getLastName();
        String name2 = player2.getFirstName() + " " + player2.getLastName();
        return Objects.equals(this.email, player2.getEmail())
                && Objects.equals(this.phoneNumber, player2.getPhoneNumber())
                && Objects.equals(name1, name2);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
