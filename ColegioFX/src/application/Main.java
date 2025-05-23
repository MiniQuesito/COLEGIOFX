package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/application/View/admin_dashboard.fxml"));
        primaryStage.setTitle("Sistema Colegio - Administrador");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
