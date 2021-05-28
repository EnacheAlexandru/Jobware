import domain.Task;
import domain.User;
import dtos.LoggedEmployee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    public void setObservableList() throws Exception {
        tasksObservableList.setAll(server.getTasksEmployee(user.getUsername()));
    }

    private final ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<Task> tasksTableView;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> importanceColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> whenDateColumn;

    @FXML
    public void initialize() {
        descriptionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        importanceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getImportance()));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        whenDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWhenDate()));
        tasksTableView.setItems(tasksObservableList);
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
    public void finishTaskHandler() throws Exception {
        Task selectedTask;

        try {
            selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
            if (selectedTask == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atentie!");
            alert.setContentText("Selectati un task!\n");
            alert.showAndWait();
            return;
        }
        if (selectedTask.getStatus().equals("Finished")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atentie!");
            alert.setContentText("Acest task a fost deja finalizat!\n");
            alert.showAndWait();
            return;
        }

        Task task = server.finishTask(user, selectedTask.getId());

        tasksObservableList.removeIf(t -> t.getId() == task.getId());
        tasksObservableList.add(task);
        tasksTableView.setItems(tasksObservableList);
    }

    @Override
    public void refreshEmployeeTask(Task task) {
        tasksObservableList.add(task);
        tasksTableView.setItems(tasksObservableList);
    }


    // UNUSED

    @Override
    public void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) {}
    @Override
    public void refreshEmployeeLoggedOut(User user, String date) {}
    @Override
    public void refreshBossTask(Task task) {}

}
