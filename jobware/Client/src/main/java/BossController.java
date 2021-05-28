import domain.Task;
import domain.User;
import dtos.LoggedEmployee;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BossController extends UnicastRemoteObject implements ObserverInterface, Serializable, ControllerInterface {
    private ServiceInterface server;
    private User user;
    private String notifications;

    public BossController() throws Exception {}

    @Override
    public void setServer(ServiceInterface server) {
        this.server = server;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public void setObservableLists() throws Exception {
        loggedEmployeesObservableList.setAll(server.getLoggedEmployees());
        tasksEmployeesObservableList.setAll(server.getTasksEmployees());
    }

    private final ObservableList<LoggedEmployee> loggedEmployeesObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<LoggedEmployee> loggedEmployeesTableView;
    @FXML
    private TableColumn<LoggedEmployee, String> nameColumn;
    @FXML
    private TableColumn<LoggedEmployee, String> dateColumn;

    private final ObservableList<Task> tasksEmployeesObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<Task> tasksEmployeesTableView;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> employeeColumn;
    @FXML
    private TableColumn<Task, String> importanceColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> whenDateColumn;

    @FXML
    private ChoiceBox<String> importanceChoiceBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label numberNotifications;
    @FXML
    private ImageView iconBell;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmployee().getName()));
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLoginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        loggedEmployeesTableView.setItems(loggedEmployeesObservableList);

        descriptionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        employeeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUser().getName()));
        importanceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getImportance()));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        whenDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWhenDate()));
        tasksEmployeesTableView.setItems(tasksEmployeesObservableList);

        importanceChoiceBox.getItems().add("Low");
        importanceChoiceBox.getItems().add("Medium");
        importanceChoiceBox.getItems().add("High");

        notifications = "";
        numberNotifications.setText("0");
        iconBell.setImage(new Image("bell.png"));
    }

    @Override
    public void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) {
        Platform.runLater(() -> {
            loggedEmployeesObservableList.add(loggedEmployee);
            loggedEmployeesTableView.setItems(loggedEmployeesObservableList);
        });
    }

    @Override
    public void refreshEmployeeLoggedOut(User user, String date) {
        Platform.runLater(() -> {
            loggedEmployeesObservableList.removeIf(u -> u.getEmployee().getUsername().equals(user.getUsername()));
            loggedEmployeesTableView.setItems(loggedEmployeesObservableList);

            notifications += user.getName() + " s-a delogat: " + date + "\n";
            int value = Integer.parseInt(numberNotifications.getText());
            value += 1;
            numberNotifications.setText(String.valueOf(value));

            iconBell.setImage(new Image("bell_notif.png"));
        });
    }

    @Override
    public void refreshBossTask(Task task) {
        Platform.runLater(() -> {
            tasksEmployeesObservableList.removeIf(t -> t.getId() == task.getId());
            tasksEmployeesObservableList.add(task);
            tasksEmployeesTableView.setItems(tasksEmployeesObservableList);
        });
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

    @FXML
    public void sendTaskHandler() throws Exception {
        String importance = importanceChoiceBox.getValue();
        String description = descriptionTextArea.getText().replaceAll("\\s+", "");
        User selectedUser = null;

        String error = "";
        if (importance == null) {
            error += "Alegeti o importanta!\n";
        }
        if (description.isBlank()) {
            error += "Scrieti o descriere!\n";
        }
        try {
            selectedUser = loggedEmployeesTableView.getSelectionModel().getSelectedItem().getEmployee();
            if (selectedUser == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            error += "Selectati un angajat!\n";
        }
        if (!error.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atentie!");
            alert.setContentText(error);
            alert.showAndWait();
            return;
        }

        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Task task = server.sendTask(selectedUser, description, importance, "Unfinished", today);

        tasksEmployeesObservableList.add(task);
        tasksEmployeesTableView.setItems(tasksEmployeesObservableList);
    }

    @FXML
    public void bellHandler() {
        if (!numberNotifications.getText().equals("0")) {
            numberNotifications.setText("0");
            iconBell.setImage(new Image("bell.png"));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatii");
            alert.setContentText(notifications);
            alert.showAndWait();

            notifications = "";
        }
    }

    // UNUSED

    @Override
    public void refreshEmployeeTask(Task task) {}
}
