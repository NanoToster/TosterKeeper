package ru.vsu.services.security.first_step;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.hands.HandKeyPointsToUser;
import ru.vsu.jpa.repositories.HandKeyPointsToUserRepository;

import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Service
public class HandServiceImpl implements HandService {
    private final HandKeyPointsToUserRepository handKeyPointsToUserRepository;

    public HandServiceImpl(HandKeyPointsToUserRepository handKeyPointsToUserRepository) {
        this.handKeyPointsToUserRepository = handKeyPointsToUserRepository;
    }

    public void addNewHandKeyPointsToUser(User user, List<String> handKeyPointList) {
        HandKeyPointsToUser handKeyPointsToUser = new HandKeyPointsToUser(user, handKeyPointList);
        handKeyPointsToUserRepository.save(handKeyPointsToUser);
    }

    @Override
    public List<HandKeyPointsToUser> findHandKeyPointsToUserListByUser(User user) {
        return handKeyPointsToUserRepository.findAllByUser(user);
    }

    @Override
    public boolean isUserAuthentic(List<HandKeyPointsToUser> handKeyPointsToUserList, List<String> handKeyPointsToCheck) {
        return handKeyPointsToUserList
                .stream()
                .anyMatch(handKeyPointsToUser -> isHandPointsEqual(handKeyPointsToUser.getHandKeyPointList(), handKeyPointsToCheck));
    }

    private boolean isHandPointsEqual(List<String> left, List<String> right) {
        Validate.isTrue(left.size() == 21, "invalid points count in left hand");
        Validate.isTrue(right.size() == 21, "invalid points count in right hand");

        System.out.println("Comparing big finger..");
        boolean isBigFingerEqual = compareFinger(left.subList(1, 5), right.subList(1, 5));
        System.out.println("Comparing Pointing finger..");
        boolean isPointingFingerEqual = compareFinger(left.subList(5, 9), right.subList(5, 9));
        System.out.println("Comparing Middle finger..");
        boolean isMiddleFingerEqual = compareFinger(left.subList(9, 13), right.subList(9, 13));
        System.out.println("Comparing NoName finger..");
        boolean isNoNameFingerEqual = compareFinger(left.subList(13, 17), right.subList(13, 17));
        System.out.println("Comparing Small finger..");
        boolean isSmallFingerEqual = compareFinger(left.subList(17, 21), right.subList(17, 21));

        List<Boolean> isFingerEqualList = List.of(isBigFingerEqual, isMiddleFingerEqual, isPointingFingerEqual, isNoNameFingerEqual, isSmallFingerEqual);
        long equalFingerCount = isFingerEqualList.stream()
                .filter(isFingerEqual -> isFingerEqual)
                .count();

        return equalFingerCount >= 3;
    }

    private boolean compareFinger(List<String> leftFinger, List<String> rightFinger) {
        Validate.isTrue(leftFinger.size() == 4, "invalid points count in left Finger");
        Validate.isTrue(rightFinger.size() == 4, "invalid points count in right Finger");

        double firstPhalanxCoefficient = calculateDistanceBetweenPoints(leftFinger.get(0), leftFinger.get(1)) / calculateDistanceBetweenPoints(rightFinger.get(0), rightFinger.get(1));
        double secondPhalanxCoefficient = calculateDistanceBetweenPoints(leftFinger.get(1), leftFinger.get(2)) / calculateDistanceBetweenPoints(rightFinger.get(1), rightFinger.get(2));
        double thirdPhalanxCoefficient = calculateDistanceBetweenPoints(leftFinger.get(2), leftFinger.get(3)) / calculateDistanceBetweenPoints(rightFinger.get(2), rightFinger.get(3));

        List<Double> phalanxCoefficientList = List.of(firstPhalanxCoefficient, secondPhalanxCoefficient, thirdPhalanxCoefficient);

        phalanxCoefficientList.forEach(System.out::println);

        long equalPhalanxCount = phalanxCoefficientList.stream()
                .filter(coefficient -> coefficient >= 0.85d && coefficient <= 1.15)
                .count();

        return equalPhalanxCount >= 2;
    }

    private double calculateDistanceBetweenPoints(String firstPointRaw, String secondPointRaw) {
        String[] firstPoint = firstPointRaw.split(":");
        String[] secondPoint = secondPointRaw.split(":");

        return Math.sqrt(
                Math.pow(Double.parseDouble(secondPoint[0]) - Double.parseDouble(firstPoint[0]), 2) +
                        Math.pow(Double.parseDouble(secondPoint[1]) - Double.parseDouble(firstPoint[1]), 2)
        );
    }
}