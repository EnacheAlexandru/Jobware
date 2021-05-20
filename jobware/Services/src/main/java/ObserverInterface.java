import domain.User;
import dtos.LoggedEmployee;

import java.rmi.Remote;

public interface ObserverInterface extends Remote {
    void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) throws Exception;
    void refreshEmployeeLoggedOut(User user) throws Exception;
}

