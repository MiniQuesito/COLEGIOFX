package application.Controller;

import application.Model.Nota;
import application.Model.Usuario;
import application.Service.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label; // Importar Label
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsultaNotasController {

    @FXML private Label lblNombreEstudiante; // Nuevo Label para mostrar el nombre del estudiante
    @FXML private TableView<Nota> tableNotasEstudiante; // Renombrado fx:id de la tabla
    @FXML private TableColumn<Nota, String> colMateriaConsulta; // Renombrado fx:id de columna
    @FXML private TableColumn<Nota, Number> colNotaConsulta; // Renombrado fx:id de columna

    private ObservableList<Nota> listaNotasDelEstudiante;
    private Usuario estudianteActual; // Para guardar el estudiante que ha iniciado sesión

    @FXML
    public void initialize() {
        // Configuración de las columnas de la tabla de notas
        colMateriaConsulta.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colNotaConsulta.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // Inicializar la ObservableList
        listaNotasDelEstudiante = FXCollections.observableArrayList();
        tableNotasEstudiante.setItems(listaNotasDelEstudiante);

        // La carga de notas se hará en setEstudiante() una vez que el objeto Usuario esté disponible
    }

    // Este método será llamado por el LoginController para pasar el estudiante logueado
    public void setEstudiante(Usuario estudiante) {
        this.estudianteActual = estudiante;
        if (estudianteActual != null) {
            lblNombreEstudiante.setText("Notas de: " + estudianteActual.getNombre());
            cargarNotasEstudiante();
        } else {
            lblNombreEstudiante.setText("Notas (Estudiante no encontrado)");
            listaNotasDelEstudiante.clear(); // Limpiar la tabla si no hay estudiante
        }
    }

    private void cargarNotasEstudiante() {
        if (estudianteActual != null) {
            // Asumiendo que DataSource.getNotasByEstudianteId(int id) ya existe y funciona
            listaNotasDelEstudiante.setAll(DataSource.getNotasByEstudianteId(estudianteActual.getId()));
        }
    }

    @FXML
    public void handleVolverAlLogin() {
        try {
            Stage stage = (Stage) tableNotasEstudiante.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/application/View/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Considera agregar un showAlert aquí si el error es crítico
        }
    }
}