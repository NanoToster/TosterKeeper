package ru.vsu.services;

import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addNewUser(String name, String email, String password) {
        userRepository.save(new User(name, email, passwordEncoder.encode(password)));
    }

    @Override
    @Nullable
    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll(Sort.by("name"));
    }
}
