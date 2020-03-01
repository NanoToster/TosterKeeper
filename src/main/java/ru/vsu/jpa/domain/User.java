package ru.vsu.jpa.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String email;
    private String passwordHash;
    private boolean isActive;

    public User() {
    }

    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isActive() {
        return isActive;
    }
}