package application.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; // Asegúrate de importar Button
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField; // Si necesitas TextField
import javafx.stage.Modality;
// Para la navegación
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import application.Main; // Para poder volver al Main o usar sus métodos si los tienes
import application.Model.Usuario; // Importa tu clase Usuario
import application.Service.DataSource;

import java.io.IOException; // Necesario para FXMLLoader

public class AdminDashboardController {

    @FXML
    private TableView<Usuario> tableUsuarios; // Tipo de dato para la tabla
    @FXML
    private TableColumn<Usuario, Integer> colIdUsuario; // Tipo de dato para la columna ID
    @FXML
    private TableColumn<Usuario, String> colNombreUsuario; // Tipo de dato para la columna Nombre
    @FXML
    private TableColumn<Usuario, String> colCorreoUsuario; // Tipo de dato para la columna Correo
    @FXML
    private TableColumn<Usuario, String> colContrasena;
    @FXML
    private TableColumn<Usuario, String> colRolUsuario; // Tipo de dato para la columna Rol

   
    @FXML
    private void handleAgregarUsuario() {
    	
        try {
            // Asegúrate de que esta ruta sea correcta para tu nuevo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View/AgregarUsuarioDialog.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Agregar Nuevo Usuario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableUsuarios.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            // Obtener el controlador del diálogo para pasarle el Stage y obtener resultados
            AgregarUsuarioDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait(); // Muestra el diálogo y espera a que se cierre

            // Si el usuario hizo clic en Guardar en el diálogo, recarga la tabla
            if (controller.isGuardarClicked()) {
                tableUsuarios.setItems(DataSource.getUsuarios()); // Recarga los usuarios
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error al cargar la ventana para agregar usuario.");
        }
    	 
    }

    @FXML
    private void handleModificarUsuario() {
    	Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View/AgregarUsuarioDialog.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Modificar Usuario"); // Cambia el título
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(tableUsuarios.getScene().getWindow());
                dialogStage.setScene(new Scene(root));

                AgregarUsuarioDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setUsuario(usuarioSeleccionado); // ¡Aquí se le pasa el usuario seleccionado!

                dialogStage.showAndWait();

                if (controller.isGuardarClicked()) {
                    tableUsuarios.setItems(DataSource.getUsuarios()); // Recarga la tabla
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Error al cargar la ventana para modificar usuario.");
            }
        } else {
            showAlert("Ningún usuario seleccionado", "Por favor, selecciona un usuario de la tabla para modificar.");
        }
    }

    @FXML
    private void handleEliminarUsuario() {
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar Usuario: " + usuarioSeleccionado.getNombre());
            alert.setContentText("¿Estás seguro de que quieres eliminar a este usuario?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                application.Service.DataSource.deleteUsuario(usuarioSeleccionado.getId());
                tableUsuarios.setItems(application.Service.DataSource.getUsuarios()); // Recargar la tabla
            }
        } else {
            showAlert("Ningún usuario seleccionado", "Por favor, selecciona un usuario de la tabla para eliminar.");
        }
    }

 // Método auxiliar para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleVolverLogin() {
        System.out.println("Volviendo al Login.");
        cargarVista("/application/View/LoginView.fxml");
    }

    // Método auxiliar para cargar vistas
    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) tableUsuarios.getScene().getWindow(); // Obtener el Stage actual
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Mostrar un mensaje de error en la UI si la vista no se puede cargar
            // lblMensaje.setText("Error al cargar la vista."); // Si tuvieras un Label de mensaje
            System.err.println("Error al cargar la vista: " + fxmlPath);
        }
    }

    @FXML
    public void initialize() {
        // Este método se llama automáticamente después de que el FXML ha sido cargado.
        // Aquí puedes inicializar la tabla, cargar datos, etc.
        System.out.println("AdminDashboardController inicializado.");

        // Configura las celdas de las columnas para mostrar las propiedades del Usuario
        colIdUsuario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombreUsuario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colCorreoUsuario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorreo()));
        colContrasena.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContrasena()));
        colRolUsuario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRol()));
        

        // Carga los usuarios de la base de datos simulada
        tableUsuarios.setItems(application.Service.DataSource.getUsuarios());
    }
}