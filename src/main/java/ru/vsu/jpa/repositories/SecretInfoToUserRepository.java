package ru.vsu.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.jpa.domain.SecretInfoToUser;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 21 March 2020
 */
public interface SecretInfoToUserRepository extends JpaRepository<SecretInfoToUser, Long> {
    Optional<SecretInfoToUser> findByUserId(long userId);
}