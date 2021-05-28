import domain.Task;
import domain.User;
import dtos.LoggedEmployee;

import java.rmi.Remote;

public interface ObserverInterface extends Remote {
    void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) throws Exception;
    void refreshEmployeeLoggedOut(User user, String date) throws Exception;

    void refreshEmployeeTask(Task task) throws Exception;
    void refreshBossTask(Task task) throws Exception;
}

