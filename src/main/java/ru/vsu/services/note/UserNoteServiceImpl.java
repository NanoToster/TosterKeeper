package ru.vsu.services.note;

import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.UserNote;
import ru.vsu.jpa.repositories.UserNoteRepository;
import ru.vsu.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ivan Rovenskiy
 * 21 March 2020
 */
@Service
public class UserNoteServiceImpl implements UserNoteService {
    private final CryptService cryptService;
    private final UserNoteRepository userNoteRepository;
    private final UserRepository userRepository;

    public UserNoteServiceImpl(CryptService cryptService, UserNoteRepository userNoteRepository, UserRepository userRepository) {
        this.cryptService = cryptService;
        this.userNoteRepository = userNoteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveAndCryptUserNote(UserNote userNote, long userId) {
        final Optional<User> userOptional = userRepository.findById(userId);

        userOptional.ifPresent(user -> userNoteRepository.save(
                UserNote.fromExistWithUpdates(userNote,
                        cryptService.encryptText(userNote.getTitle(), user),
                        cryptService.encryptText(userNote.getBody(), user))));

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User can'r be found for note crypt");
        }
    }

    @Override
    public List<UserNote> findAllAndDecryptUserNoteList(long userId) {
        final Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User can'r be found for note crypt");
        }

        final List<UserNote> allEncryptedNoteList = userNoteRepository.findAllByUserId(userId);

        return allEncryptedNoteList.stream()
                .map(encryptedNote -> UserNote.fromExistWithUpdates(
                        encryptedNote,
                        cryptService.decryptText(encryptedNote.getTitle(), userOptional.get()),
                        cryptService.decryptText(encryptedNote.getBody(), userOptional.get())))
                .collect(Collectors.toList());
    }
}