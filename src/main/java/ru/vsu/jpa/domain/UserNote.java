package ru.vsu.jpa.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ivan Rovenskiy
 * 19 March 2020
 */
@Entity
public class UserNote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private Date creationDate;
    @Lob
    private String title;
    @Lob
    private String body;
    private boolean isActive;

    public UserNote() {
    }

    public static UserNote deactivate(UserNote userNote) {
        userNote.isActive = false;
        return userNote;
    }

    public static UserNote newNote(long userId, Date creationDate, String title, String body) {
        return new UserNote(userId, creationDate, title, body, true);
    }

    public static UserNote fromExistWithUpdates(UserNote exist, String newTitle, String newBody) {
        return new UserNote(
                exist.getId(),
                exist.getUserId(),
                exist.getCreationDate(),
                newTitle,
                newBody,
                true);
    }

    private UserNote(long userId, Date creationDate, String title, String body, boolean isActive) {
        this.userId = userId;
        this.creationDate = creationDate;
        this.title = title;
        this.body = body;
        this.isActive = isActive;
    }

    public UserNote(long id, long userId, Date creationDate, String title, String body, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.creationDate = creationDate;
        this.title = title;
        this.body = body;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isActive() {
        return isActive;
    }
}