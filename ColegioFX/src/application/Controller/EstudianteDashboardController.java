package application.Controller; // Asegúrate de que el paquete sea correcto

import application.Model.Nota;
import application.Model.Usuario;
import application.Service.DataSource; // Importa tu clase DataSource
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Si usas un botón
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class EstudianteDashboardController { // Este es el nombre de tu controlador

    @FXML private Label lblNombre;
    @FXML private Label lblID;
    @FXML private TableView<Nota> tableNotasEstudiante;
    @FXML private TableColumn<Nota, String> colMateriaEstudiante;
    @FXML private TableColumn<Nota, Number> colNotaEstudiante; // O Double, si la nota es numérica

    private Usuario estudianteLogueado;
    private ObservableList<Nota> misNotas;

    @FXML
    public void initialize() {
        // Inicialización de las columnas de la tabla de notas
        colMateriaEstudiante.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colNotaEstudiante.setCellValueFactory(new PropertyValueFactory<>("valor")); // Asumiendo que el atributo de Nota es 'valor'

        // Inicializar la ObservableList para la tabla
        misNotas = FXCollections.observableArrayList();
        tableNotasEstudiante.setItems(misNotas);
    }

    // Método para recibir el objeto Usuario (el estudiante logueado)
    public void setEstudiante(Usuario estudiante) {
        this.estudianteLogueado = estudiante;
        if (estudianteLogueado != null) {
            lblNombre.setText(estudianteLogueado.getNombre());
            lblID.setText(String.valueOf(estudianteLogueado.getId()));
            cargarMisNotas(); // Carga las notas del estudiante una vez que ha sido seteado
        }
    }

    // Método para cargar las notas del estudiante logueado
    private void cargarMisNotas() {
        if (estudianteLogueado != null) {
            // Usar el método de DataSource para obtener las notas por ID de estudiante
            misNotas.setAll(DataSource.getNotasByEstudianteId(estudianteLogueado.getId()));
        } else {
            misNotas.clear(); // Limpiar si no hay estudiante
        }
    }

    @FXML
    public void handleVolverLogin() { // Nombre del método según tu FXML
        try {
            Stage stage = (Stage) lblNombre.getScene().getWindow(); // Cualquier elemento de la escena
            Parent root = FXMLLoader.load(getClass().getResource("/application/View/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de error si no se puede cargar la vista de login
            System.err.println("Error al volver al login: " + e.getMessage());
        }
    }
}