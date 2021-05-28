import domain.User;

public interface UserRepositoryInterface {
    User getByUsername(String username);
}
