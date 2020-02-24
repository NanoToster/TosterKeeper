package ru.vsu.services;

import ru.vsu.jpa.domain.HandInformation;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface HandService {
    void addNewHand(HandInformation handInformation);

    Optional<HandInformation> findHandById(long handId);
}
