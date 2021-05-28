import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartClient extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring_client.xml");
            ServiceInterface server = (ServiceInterface) factory.getBean("srv");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setServer(server);

            primaryStage.setOnCloseRequest(event -> System.exit(0));

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login");
            primaryStage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
