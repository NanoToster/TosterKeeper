package ru.vsu.jpa.domain;

import javax.persistence.*;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Entity
public class GoogleAuthToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    private String userSecret;

    public GoogleAuthToUser() {
    }

    public GoogleAuthToUser(User user, String userSecret) {
        this.user = user;
        this.userSecret = userSecret;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getUserSecret() {
        return userSecret;
    }
}