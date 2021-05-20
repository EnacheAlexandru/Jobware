import domain.User;
import dtos.LoggedEmployee;
import javafx.fxml.FXML;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;

public class EmployeeController extends UnicastRemoteObject implements ObserverInterface, Serializable, ControllerInterface {
    private ServiceInterface server;
    private User user;

    public EmployeeController() throws Exception {}

    @Override
    public void setServer(ServiceInterface server) {
        this.server = server;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void logout() {
        try {
            server.logout(user.getUsername(), this);
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Logout error " + e);
        }
    }

    @FXML
    public void logoutButtonHandler() {
        logout();
    }

    // UNUSED

    @Override
    public void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) { }
    @Override
    public void refreshEmployeeLoggedOut(User user) { }
}
