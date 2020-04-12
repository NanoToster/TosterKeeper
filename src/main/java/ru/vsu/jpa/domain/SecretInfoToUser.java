package ru.vsu.jpa.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.persistence.*;

/**
 * @author Ivan Rovenskiy
 * 21 March 2020
 */
@Entity
public class SecretInfoToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    @Lob
    private String userSecret;
    @Lob
    private String salt;

    public SecretInfoToUser() {
    }

    public SecretInfoToUser(User user) {
        this.userId = user.getId();
        this.userSecret = RandomStringUtils.randomAlphabetic(256);
        this.salt = KeyGenerators.string().generateKey();
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public String getSalt() {
        return salt;
    }
}