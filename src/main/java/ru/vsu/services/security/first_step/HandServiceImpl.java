package ru.vsu.services.security.first_step;

import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.HandInformation;
import ru.vsu.jpa.repositories.HandRepository;

import java.util.Optional;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Service
public class HandServiceImpl implements HandService {
    private final HandRepository handRepository;

    public HandServiceImpl(HandRepository handRepository) {
        this.handRepository = handRepository;
    }

    public void addNewHand(HandInformation handInformation) {
        handRepository.save(handInformation);
    }

    @Override
    public Optional<HandInformation> findHandById(long handId) {
        return handRepository.findById(handId);
    }
}