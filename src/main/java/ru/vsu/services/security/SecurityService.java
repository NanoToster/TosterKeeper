package ru.vsu.services.security;

import ru.vsu.jpa.domain.GoogleAuthToUser;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
public interface SecurityService {
    String generateUserSecretForGoogleAuth();

    GoogleAuthToUser findGoogleAuthByUserId(long userId);
}