package ru.vsu.services;

import ru.vsu.jpa.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface UserService {
    void addNewUser(String name, String email, String password, String userSecretForGoogleAuth);

    Optional<User> findUserById(long userId);

    List<User> findAllActiveUsers();
}