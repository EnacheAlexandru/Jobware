import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private javafx.scene.control.TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;

    private ServiceInterface server;

    public void setServer(ServiceInterface server) {
        this.server = server;
    }

    public LoginController() {}

    @FXML
    public void loginButtonHandler() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        User user;
        try {
            user = server.checkLogin(username, password);
        }
        catch (Exception e) {
            this.showNotification(e.getMessage(), Alert.AlertType.WARNING);
            return;
        }
        try {
            if (user.getRank().equals("Employee")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeWindow.fxml"));
                Parent root = loader.load();
                EmployeeController ctrl = loader.getController();
                ctrl.setServer(server);
                ctrl.setUser(user);
                server.login(user.getUsername(), ctrl);

                Stage stage = new Stage();
                stage.setTitle("Utilizator: " + username);
                stage.setScene(new Scene(root));
                stage.setOnCloseRequest(event -> {
                    ctrl.logout();
                    System.exit(0);
                });
                stage.show();
            }
            else if (user.getRank().equals("Boss")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BossWindow.fxml"));
                Parent root = loader.load();
                BossController ctrl = loader.getController();
                ctrl.setServer(server);
                ctrl.setUser(user);
                server.login(user.getUsername(), ctrl);

                Stage stage = new Stage();
                stage.setTitle("Utilizator: " + username);
                stage.setScene(new Scene(root));
                stage.setOnCloseRequest(event -> {
                    ctrl.logout();
                    System.exit(0);
                });
                stage.show();

                ctrl.setLoggedEmployees();
            }

            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        }
        catch (Exception e) {
            this.showNotification("Eroare.\n" + e, Alert.AlertType.ERROR);
        }
    }

    private void showNotification(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle("Notificare");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
