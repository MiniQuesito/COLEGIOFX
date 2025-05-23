package application.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class LoginController {
	
	    @FXML
	    private TextField txtUsuario;

	    @FXML
	    private PasswordField txtPassword;

	    @FXML
	    private Button btnIngresar;

	    @FXML
	    private Label lblMensaje;

	    @FXML
	    public void initialize() {
	        lblMensaje.setText("");
	    }

	    @FXML
	    private void handleLogin() {
	        String usuario = txtUsuario.getText();
	        String password = txtPassword.getText();

	        // Aquí iría la validación con base de datos o sistema real
	        if (usuario.equals("admin") && password.equals("1234")) {
	            lblMensaje.setText("Ingreso exitoso como Administrador");
	            // Aquí puedes cargar la vista del dashboard
	        } else {
	            lblMensaje.setText("Usuario o contraseña incorrectos");
	        }
	    }



}
