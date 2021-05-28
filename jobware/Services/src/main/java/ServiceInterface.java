import domain.Task;
import domain.User;
import dtos.LoggedEmployee;

import java.util.List;

public interface ServiceInterface {
    User checkLogin(String username, String password) throws Exception;
    void login(String username, ObserverInterface client) throws Exception;
    void logout(String username, ObserverInterface client) throws Exception;
    List<LoggedEmployee> getLoggedEmployees() throws Exception;
    List<Task> getTasksEmployees() throws Exception;
    List<Task> getTasksEmployee(String username) throws Exception;
    Task sendTask(User user, String description, String importance, String status, String whenDate) throws Exception;
    Task finishTask(User user, int taskID) throws Exception;
}
