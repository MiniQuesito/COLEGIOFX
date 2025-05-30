package application.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import application.Model.Nota;
import application.Model.Usuario; // Importar Usuario
import application.Service.DataSource; // Importar DataSource
import javafx.scene.control.cell.PropertyValueFactory; // Asegúrate de que esto está importado

import java.io.IOException;

public class NotasController {

    @FXML private TableView<Nota> tableNotas;
    @FXML private TableColumn<Nota, Number> colIdEstudiante;
    @FXML private TableColumn<Nota, String> colNombreEstudiante;
    @FXML private TableColumn<Nota, String> colMateria;
    @FXML private TableColumn<Nota, Number> colNota;
    @FXML private TextField txtIdEstudiante;
    @FXML private TextField txtNombreEstudiante;
    @FXML private TextField txtMateria;
    @FXML private TextField txtNota;

    private ObservableList<Nota> listaNotas;

    // Campo para almacenar el estudiante actual
    private Usuario estudianteActual;

    // Método para recibir el estudiante desde ProfesoresController
    public void setEstudiante(Usuario estudiante) {
        this.estudianteActual = estudiante;
        if (estudianteActual != null) {
            txtIdEstudiante.setText(String.valueOf(estudianteActual.getId()));
            txtNombreEstudiante.setText(estudianteActual.getNombre());
            // Deshabilitar estos campos para que no se puedan modificar directamente
            txtIdEstudiante.setEditable(false);
            txtNombreEstudiante.setEditable(false);

            // Cargar notas solo para este estudiante usando tu método DataSource
            listaNotas.setAll(DataSource.getNotasByEstudianteId(estudianteActual.getId()));
        }
    }


    @FXML
    public void initialize() {
        // Configuración de las celdas de las columnas
        colIdEstudiante.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("valor"));

        listaNotas = FXCollections.observableArrayList();
        tableNotas.setItems(listaNotas);

        // Listener para la selección de la tabla, para rellenar los campos de texto
        tableNotas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIdEstudiante.setText(String.valueOf(newSelection.getIdEstudiante()));
                txtNombreEstudiante.setText(newSelection.getNombreEstudiante()); // Asegúrate que Nota tiene getNombreEstudiante()
                txtMateria.setText(newSelection.getMateria());
                txtNota.setText(String.valueOf(newSelection.getValor()));
            } else {
                // Limpiar campos si no hay selección (mantener ID y Nombre del estudiante actual si está en modo edición)
                // txtIdEstudiante.clear(); // No borrar si ya está pre-cargado
                // txtNombreEstudiante.clear(); // No borrar si ya está pre-cargado
                txtMateria.clear();
                txtNota.clear();
            }
        });
    }

    // Métodos para la gestión de notas
    @FXML
    public void handleAgregar() {
        if (estudianteActual == null) {
            showAlert("Error", "No hay estudiante seleccionado para agregar notas.");
            return;
        }

        try {
            // int idEstudiante = Integer.parseInt(txtIdEstudiante.getText()); // No necesario si ya tenemos estudianteActual
            String materia = txtMateria.getText();
            double notaValor = Double.parseDouble(txtNota.getText());

            if (materia.isEmpty()) {
                showAlert("Campos vacíos", "Por favor, ingresa la materia.");
                return;
            }

            // Crear la nota con los datos del estudiante actual
            Nota nuevaNota = new Nota(estudianteActual.getId(), estudianteActual.getNombre(), materia, notaValor);

            // Usar tu DataSource para agregar la nota
            DataSource.addNota(nuevaNota);

            // Recargar la tabla para el estudiante actual (usando tu método getNotasByEstudianteId)
            listaNotas.setAll(DataSource.getNotasByEstudianteId(estudianteActual.getId()));

            // Limpiar campos después de agregar
            txtMateria.clear();
            txtNota.clear();
            showAlert("Éxito", "Nota agregada correctamente.");

        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "Por favor, ingresa una nota numérica válida.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al agregar la nota: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditar() {
        Nota seleccionada = tableNotas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                // No se edita el ID del estudiante ni el nombre del estudiante de una nota existente
                seleccionada.setMateria(txtMateria.getText());
                seleccionada.setValor(Double.parseDouble(txtNota.getText()));

                // Usar tu DataSource para actualizar la nota
                DataSource.updateNota(seleccionada);

                tableNotas.refresh(); // Refrescar la vista de la tabla para mostrar los cambios
                showAlert("Éxito", "Nota editada correctamente.");
            } catch (NumberFormatException e) {
                showAlert("Entrada Inválida", "Por favor, ingresa una nota numérica válida.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Ocurrió un error al editar la nota.");
            }
        } else {
            showAlert("Ninguna nota seleccionada", "Por favor, selecciona una nota de la tabla para editar.");
        }
    }

    @FXML
    public void handleEliminar() {
        Nota seleccionada = tableNotas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar Nota");
            alert.setContentText("¿Estás seguro de que quieres eliminar esta nota?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                // Usar tu DataSource para eliminar la nota
                // Asumiendo que has modificado deleteNota para aceptar un ID, o usar deleteNota(Nota nota)
                DataSource.deleteNota(seleccionada.getId()); // Si tu deleteNota acepta int
                // O si tu deleteNota acepta el objeto Nota: DataSource.deleteNota(seleccionada);

                listaNotas.remove(seleccionada); // Quitarla de la ObservableList
                showAlert("Éxito", "Nota eliminada correctamente.");
            }
        } else {
            showAlert("Ninguna nota seleccionada", "Por favor, selecciona una nota de la tabla para eliminar.");
        }
    }

    @FXML
    public void handleVolver() {
        try {
            Stage stage = (Stage) tableNotas.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View/ProfesorView.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo volver a la vista del profesor.");
        }
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Cambiado a INFORMATION para ser menos intrusivo
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}