import domain.User;
import dtos.LoggedEmployee;

import java.util.List;

public interface ServiceInterface {
    User checkLogin(String username, String password) throws Exception;
    void login(String username, ObserverInterface client) throws Exception;
    void logout(String username, ObserverInterface client) throws Exception;
    List<LoggedEmployee> getLoggedEmployees() throws Exception;
}
