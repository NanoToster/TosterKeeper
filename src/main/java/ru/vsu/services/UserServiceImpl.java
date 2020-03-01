package ru.vsu.services;

import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.GoogleAuthToUser;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.repositories.GoogleAuthRepository;
import ru.vsu.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleAuthRepository googleAuthRepository;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, GoogleAuthRepository googleAuthRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.googleAuthRepository = googleAuthRepository;
    }

    @Override
    public void addNewUser(String name, String email, String password, String userSecretForGoogleAuth) {
        final User savedUser = userRepository.save(new User(name, email, passwordEncoder.encode(password)));

        googleAuthRepository.save(new GoogleAuthToUser(savedUser, userSecretForGoogleAuth));
    }

    @Override
    @Nullable
    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllActiveUsers() {
        return userRepository.findAll(Sort.by("name")).stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
}