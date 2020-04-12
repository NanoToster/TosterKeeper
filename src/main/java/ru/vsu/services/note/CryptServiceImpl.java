package ru.vsu.services.note;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.SecretInfoToUser;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.repositories.SecretInfoToUserRepository;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 21 March 2020
 */
@Service
public class CryptServiceImpl implements CryptService {
    private final SecretInfoToUserRepository secretInfoToUserRepository;

    public CryptServiceImpl(SecretInfoToUserRepository secretInfoToUserRepository) {
        this.secretInfoToUserRepository = secretInfoToUserRepository;
    }

    @Override
    public String encryptText(String decryptedText, User user) {
        final Optional<SecretInfoToUser> secretInfoOptional = secretInfoToUserRepository.findByUserId(user.getId());

        if (secretInfoOptional.isEmpty()) {
            return "";
        } else {
            return Encryptors.text(secretInfoOptional.get().getUserSecret(), secretInfoOptional.get().getSalt())
                    .encrypt(decryptedText);
        }
    }

    @Override
    public String decryptText(String encryptedText, User user) {
        final Optional<SecretInfoToUser> secretInfoOptional = secretInfoToUserRepository.findByUserId(user.getId());

        if (secretInfoOptional.isEmpty()) {
            return "";
        } else {
            return Encryptors.text(secretInfoOptional.get().getUserSecret(), secretInfoOptional.get().getSalt())
                    .decrypt(encryptedText);
        }
    }

    @Override
    public void generateAndSaveUserSecretInfo(User user) {
        secretInfoToUserRepository.save(new SecretInfoToUser(user));
    }
}