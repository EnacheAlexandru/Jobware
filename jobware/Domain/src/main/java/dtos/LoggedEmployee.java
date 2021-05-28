package dtos;

import domain.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoggedEmployee implements Serializable {
    private final User employee;
    private final LocalDateTime loginTime;

    public LoggedEmployee(User employee, LocalDateTime loginTime) {
        this.employee = employee;
        this.loginTime = loginTime;
    }

    public User getEmployee() {
        return employee;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
}

