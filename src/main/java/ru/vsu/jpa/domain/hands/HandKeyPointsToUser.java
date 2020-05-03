package ru.vsu.jpa.domain.hands;

import ru.vsu.jpa.domain.User;

import javax.persistence.*;
import java.util.List;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Entity
public class HandKeyPointsToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> handKeyPointList;

    public HandKeyPointsToUser() {
    }

    public HandKeyPointsToUser(User user, List<String> handKeyPointList) {
        this.user = user;
        this.handKeyPointList = handKeyPointList;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<String> getHandKeyPointList() {
        return handKeyPointList;
    }
}