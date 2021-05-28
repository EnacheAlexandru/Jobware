package domain;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private User user;
    private String username;
    private String description;
    private String importance;
    private String status;
    private String whenDate;

    public Task() {}

    public Task(User user, String description, String importance, String status, String whenDate) {
        this.user = user;
        this.username = user.getUsername();
        this.description = description;
        this.importance = importance;
        this.status = status;
        this.whenDate = whenDate;
    }

    public Task(int id, User user, String description, String importance, String status, String whenDate) {
        this.id = id;
        this.user = user;
        this.username = user.getUsername();
        this.description = description;
        this.importance = importance;
        this.status = status;
        this.whenDate = whenDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhenDate() {
        return whenDate;
    }

    public void setWhenDate(String whenDate) {
        this.whenDate = whenDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
