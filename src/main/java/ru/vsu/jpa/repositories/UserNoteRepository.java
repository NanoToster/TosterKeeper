package ru.vsu.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.jpa.domain.UserNote;

import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 20 March 2020
 */
public interface UserNoteRepository extends JpaRepository<UserNote, Long> {
    List<UserNote> findAllByUserId(long userId);
}