import domain.User;

public interface ControllerInterface {
    void setServer(ServiceInterface server);
    void setUser(User user);
    void logout();
}
