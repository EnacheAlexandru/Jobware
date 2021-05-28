import domain.Task;
import domain.User;
import dtos.LoggedEmployee;
import utils.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements ServiceInterface {
    private final int n_threads = 5;
    private final UserRepositoryInterface userRepo;
    private final TaskRepositoryInterface taskRepo;
    private final Map<String, Pair<ObserverInterface, LocalDateTime>> loggedEmployees;
    private ObserverInterface loggedBoss;

    public Service(UserRepositoryInterface userRepo, TaskRepositoryInterface taskRepo) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        loggedEmployees = new ConcurrentHashMap<>();
        loggedBoss = null;
    }

    @Override
    public synchronized User checkLogin(String username, String password) throws Exception {
        User user = userRepo.getByUsername(username);
        System.out.println(user);
        if (user == null){
            throw new Exception("Autentificarea a esuat.");
        }
        if (!password.equals(user.getPassword())){
            throw new Exception("Autentificarea a esuat.");
        }
        if (user.getRank().equals("Employee") && loggedEmployees.get(username) != null) {
            throw new Exception("User deja logat.");
        }
        else if (user.getRank().equals("Boss") && loggedBoss != null) {
            throw new Exception("User deja logat.");
        }
        return user;
    }

    @Override
    public synchronized void login(String username, ObserverInterface client) {
        User user = userRepo.getByUsername(username);
        LocalDateTime loginTime = LocalDateTime.now();
        if (user.getRank().equals("Employee")) {
            loggedEmployees.put(username, new Pair<>(client, loginTime));
            if (loggedBoss != null) {
                this.notifyEmployeeLoggedIn(new LoggedEmployee(user, loginTime));
            }
        }
        else if (user.getRank().equals("Boss")) {
            loggedBoss = client;
        }
    }

    @Override
    public synchronized void logout(String username, ObserverInterface client) {
        User user = userRepo.getByUsername(username);
        if (user.getRank().equals("Employee")) {
            loggedEmployees.remove(username);
            if (loggedBoss != null) {
                this.notifyEmployeeLoggedOut(user);
            }
        }
        else if (user.getRank().equals("Boss")) {
            loggedBoss = null;
        }
    }

    @Override
    public synchronized List<LoggedEmployee> getLoggedEmployees() {
        List<LoggedEmployee> loggedUsers = new ArrayList<>();
        for (String username : loggedEmployees.keySet()) {
            User user = userRepo.getByUsername(username);
            if (user.getRank().equals("Employee")) {
                loggedUsers.add(new LoggedEmployee(user, loggedEmployees.get(username).getValue()));
            }
        }
        return loggedUsers;
    }

    @Override
    public synchronized List<Task> getTasksEmployees() {
        List<Task> tasks = taskRepo.getTasks();
        for (Task task : tasks) {
            task.setUser(userRepo.getByUsername(task.getUsername()));
        }
        return tasks;
    }

    @Override
    public synchronized List<Task> getTasksEmployee(String username) {
        List<Task> tasks = taskRepo.getTasksByUsername(username);
        for (Task task : tasks) {
            task.setUser(userRepo.getByUsername(username));
        }
        return tasks;
    }

    @Override
    public synchronized Task sendTask(User user, String description, String importance, String status, String whenDate) {
        Task task = taskRepo.saveTask(user, description, importance, status, whenDate);
        this.notifyEmployeeTask(task);
        return task;
    }

    @Override
    public Task finishTask(User user, int taskID) {
        Task task = taskRepo.setStatus(taskID, true);
        task.setUser(user);
        this.notifyBossTask(task);
        return task;
    }


    // REFRESHES

    private void notifyEmployeeLoggedIn(LoggedEmployee loggedEmployee) {
        ExecutorService executor = Executors.newFixedThreadPool(this.n_threads);
        executor.execute(() -> {
            try {
                loggedBoss.refreshEmployeeLoggedIn(loggedEmployee);
            } catch (Exception e) {
                System.err.println("Error notifying " + e.getMessage());
            }
        });
        executor.shutdown();
    }

    private void notifyEmployeeLoggedOut(User user) {
        ExecutorService executor = Executors.newFixedThreadPool(this.n_threads);
        executor.execute(() -> {
            try {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                loggedBoss.refreshEmployeeLoggedOut(user, now);
            } catch (Exception e) {
                System.err.println("Error notifying " + e.getMessage());
            }
        });
        executor.shutdown();
    }

    private void notifyEmployeeTask(Task task) {
        ExecutorService executor = Executors.newFixedThreadPool(this.n_threads);
        executor.execute(() -> {
            try {
                Pair<ObserverInterface, LocalDateTime> loggedEmployee = loggedEmployees.get(task.getUsername());
                if (loggedEmployee != null) {
                    loggedEmployee.getKey().refreshEmployeeTask(task);
                }
            } catch (Exception e) {
                System.err.println("Error notifying " + e.getMessage());
            }
        });
        executor.shutdown();
    }

    private void notifyBossTask(Task task) {
        ExecutorService executor = Executors.newFixedThreadPool(this.n_threads);
        executor.execute(() -> {
            try {
                if (loggedBoss != null) {
                    loggedBoss.refreshBossTask(task);
                }
            } catch (Exception e) {
                System.err.println("Error notifying " + e.getMessage());
            }
        });
        executor.shutdown();
    }
}
