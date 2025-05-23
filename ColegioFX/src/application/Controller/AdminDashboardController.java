package application.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class AdminDashboardController {

	    @FXML
	    private StackPane centerPane;

	    @FXML
	    private Button btnEstudiantes;

	    @FXML
	    private Button btnProfesores;

	    @FXML
	    private Button btnCerrar;

	    @FXML
	    private ImageView imageBanner;

	    @FXML
	    private ImageView imagePerfil;

	    @FXML
	    private Button btnUsuario;

	    @FXML
	    private void handleEstudiantes() {
	        loadCenterContent("/gui/views/estudiantes.fxml");
	    }

	    private void loadCenterContent(String fxmlPath) {
	        try {
	            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
	            centerPane.getChildren().setAll(node);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	
	
	

