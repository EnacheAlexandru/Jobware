import domain.User;
import dtos.LoggedEmployee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BossController extends UnicastRemoteObject implements ObserverInterface, Serializable, ControllerInterface {
    private ServiceInterface server;
    private User user;

    public BossController() throws Exception {}

    @Override
    public void setServer(ServiceInterface server) {
        this.server = server;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public void setLoggedEmployees() throws Exception {
        loggedEmployeesObservableList.setAll(server.getLoggedEmployees());
    }

    private final ObservableList<LoggedEmployee> loggedEmployeesObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<LoggedEmployee> loggedEmployeesTableView;
    @FXML
    private TableColumn<LoggedEmployee, String> nameColumn;
    @FXML
    private TableColumn<LoggedEmployee, String> dateColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmployee().getName()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLoginTime().format(formatter)));
        loggedEmployeesTableView.setItems(loggedEmployeesObservableList);
    }

    @Override
    public void refreshEmployeeLoggedIn(LoggedEmployee loggedEmployee) {
        loggedEmployeesObservableList.add(loggedEmployee);
        loggedEmployeesTableView.setItems(loggedEmployeesObservableList);
    }

    @Override
    public void refreshEmployeeLoggedOut(User user) {
        loggedEmployeesObservableList.removeIf(u -> u.getEmployee().getUsername().equals(user.getUsername()));
        loggedEmployeesTableView.setItems(loggedEmployeesObservableList);
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
}
