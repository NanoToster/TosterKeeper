package ru.vsu.services.note;

import ru.vsu.jpa.domain.User;

/**
 * @author Ivan Rovenskiy
 * 21 March 2020
 */
public interface CryptService {
    String encryptText(String decryptedText, User user);

    String decryptText(String encryptedText, User user);

    void generateAndSaveUserSecretInfo(User user);
}
