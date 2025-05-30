package application.Controller;

import application.Model.Usuario;
import application.Service.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblMensajeError; // Recomiendo usar lblMensajeError para los mensajes de error de login

    @FXML
    private void handleLogin() {
        String username = txtUsuario.getText();
        String password = txtContrasena.getText();

        Usuario usuarioAutenticado = DataSource.authenticateUser(username, password);

        if (usuarioAutenticado != null) {
            System.out.println("Login exitoso para: " + usuarioAutenticado.getNombre() + " con rol: " + usuarioAutenticado.getRol());

            cargarVista(usuarioAutenticado);
        } else {
            lblMensajeError.setText("Usuario o contraseña incorrectos."); 
        }
    }

    // Método actualizado para cargar la vista según el rol del usuario
    private void cargarVista(Usuario usuario) {
        try {
            String fxmlPath = "";
            Stage stage = (Stage) txtUsuario.getScene().getWindow(); // Obtiene la etapa actual
            FXMLLoader loader = new FXMLLoader(); // Se crea una nueva instancia de FXMLLoader

            if (usuario.getRol().equals("admin")) {
                fxmlPath = "/application/View/AdminDashboard.fxml";
                loader.setLocation(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                // Si el AdminDashboardController necesita el objeto Usuario, lo pasarías aquí:
                // AdminDashboardController adminController = loader.getController();
                // adminController.setAdmin(usuario);
                stage.setScene(new Scene(root));
                stage.setTitle("Panel de Administración");
            } else if (usuario.getRol().equals("profesor")) {
                fxmlPath = "/application/View/ProfesorView.fxml"; // La vista unificada del profesor
                loader.setLocation(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                // El ProfesorController no necesita el objeto Usuario directamente al iniciar,
                // ya que su funcionalidad se centra en los estudiantes y sus notas.
                stage.setScene(new Scene(root));
                stage.setTitle("Gestión de Notas - Profesor");
            } else if (usuario.getRol().equals("estudiante")) {
                fxmlPath = "/application/View/estudiantes.fxml"; // Asegúrate que esta ruta es correcta
                loader.setLocation(getClass().getResource(fxmlPath)); // <-- SET LOCATION
                Parent root = loader.load(); // <-- LOAD
                EstudianteDashboardController estudianteController = loader.getController();
                estudianteController.setEstudiante(usuario);
                stage.setScene(new Scene(root));
                stage.setTitle("Mis Notas");
            } else {
                // Si el rol no coincide con ninguno, muestra un mensaje de error
                showAlert("Error de Rol", "El rol del usuario no está definido para cargar una vista.");
                return; // Salir del método
            }

            stage.show(); // Mostrar la etapa después de configurar la escena
        } catch (IOException e) {
            e.printStackTrace(); // Imprime la traza de la excepción completa en la consola
            showAlert("Error al cargar la vista", "No se pudo cargar la vista para el rol: " + usuario.getRol() + ". Detalles: " + e.getMessage());
        } catch (Exception e) { // Captura cualquier otra excepción inesperada
            e.printStackTrace();
            showAlert("Error inesperado", "Ocurrió un error inesperado al cargar la vista.");
        }
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}