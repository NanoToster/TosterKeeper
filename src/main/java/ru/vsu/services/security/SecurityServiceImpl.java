package ru.vsu.services.security;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.GoogleAuthToUser;
import ru.vsu.jpa.repositories.GoogleAuthRepository;

import java.security.SecureRandom;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    private final GoogleAuthRepository googleAuthRepository;

    public SecurityServiceImpl(GoogleAuthRepository googleAuthRepository) {
        this.googleAuthRepository = googleAuthRepository;
    }

    @Override
    public String generateUserSecretForGoogleAuth() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    @Override
    public GoogleAuthToUser findGoogleAuthByUserId(long userId) {
        return googleAuthRepository.findByUserId(userId);
    }
}