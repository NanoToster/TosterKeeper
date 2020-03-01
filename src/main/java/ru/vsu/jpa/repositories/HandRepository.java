package ru.vsu.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.jpa.domain.HandInformation;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface HandRepository extends JpaRepository<HandInformation, Long> {
}