package ru.vsu.services.note;

import ru.vsu.jpa.domain.UserNote;

import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 20 March 2020
 */
public interface UserNoteService {
    void saveAndCryptUserNote(UserNote userNote, long userId);

    List<UserNote> findAllAndDecryptUserNoteList(long userId);
}