package ru.vsu.services.security.first_step;

import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.hands.HandKeyPointsToUser;

import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
public interface HandService {
    void addNewHandKeyPointsToUser(User user, List<String> handKeyPointList);

    List<HandKeyPointsToUser> findHandKeyPointsToUserListByUser(User user);

    boolean isUserAuthentic(List<HandKeyPointsToUser> handKeyPointsToUserList, List<String> handKeyPointsToCheck);
}