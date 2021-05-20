import domain.User;
import dtos.LoggedEmployee;
import utils.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements ServiceInterface {
    private final int n_threads = 5;
    private final UserRepositoryInterface userRepo;
    private final Map<String, Pair<ObserverInterface, LocalDateTime>> loggedEmployees;
    private ObserverInterface loggedBoss;

    public Service(UserRepositoryInterface userRepo) {
        this.userRepo = userRepo;
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
                loggedBoss.refreshEmployeeLoggedOut(user);
            } catch (Exception e) {
                System.err.println("Error notifying " + e.getMessage());
            }
        });
        executor.shutdown();
    }
}
