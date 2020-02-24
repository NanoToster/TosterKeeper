package ru.vsu.services;

import ru.vsu.jpa.domain.User;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface UserService {
    void addNewUser(User user);

    Optional<User> findUserById(long userId);
}
