package ru.vsu.jpa.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Ivan Rovenskiy
 * 23 February 2020
 */
@Entity
public class HandInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    public HandInformation() {
    }

    public HandInformation(User user) {
        this.user = user;
    }
}
