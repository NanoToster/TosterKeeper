package ru.vsu.services;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.repositories.UserRepository;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addNewUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Nullable
    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }
}
