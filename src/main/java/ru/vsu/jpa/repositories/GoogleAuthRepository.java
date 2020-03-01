package ru.vsu.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.jpa.domain.GoogleAuthToUser;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
public interface GoogleAuthRepository extends JpaRepository<GoogleAuthToUser, Long> {
    GoogleAuthToUser findByUserId(long userId);
}