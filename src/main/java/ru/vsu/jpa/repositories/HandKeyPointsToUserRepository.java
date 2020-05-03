package ru.vsu.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.hands.HandKeyPointsToUser;

import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface HandKeyPointsToUserRepository extends JpaRepository<HandKeyPointsToUser, Long> {
    List<HandKeyPointsToUser> findAllByUser(User user);
}