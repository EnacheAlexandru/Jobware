import domain.Task;
import domain.User;

import java.util.List;

public interface TaskRepositoryInterface {
    Task saveTask(User user, String description, String importance, String status, String whenDate);
    List<Task> getTasksByUsername(String username);
    List<Task> getTasks();
    Task setStatus(int taskID, boolean status);
}
