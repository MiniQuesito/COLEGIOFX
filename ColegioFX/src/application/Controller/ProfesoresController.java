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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfesoresController {

    @FXML private TextField txtBuscarEstudiante;
    @FXML private TableView<Usuario> tableEstudiantesProfesor;
    @FXML private TableColumn<Usuario, Number> colIdEstudianteProfesor;
    @FXML private TableColumn<Usuario, String> colNombreEstudianteProfesor;
    @FXML private TableColumn<Usuario, String> colCorreoEstudianteProfesor;
    @FXML private TableColumn<Usuario, String> colRolEstudianteProfesor; // Para mostrar el rol

    // Elementos de la sección de notas (nuevos fx:id en ProfesorView.fxml)
    @FXML private TextField txtIdEstudianteNotas;
    @FXML private TextField txtNombreEstudianteNotas;
    @FXML private TextField txtMateria;
    @FXML private TextField txtNota;
    @FXML private TableView<Nota> tableNotas;
    @FXML private TableColumn<Nota, String> colMateriaNotas;
    @FXML private TableColumn<Nota, Number> colValorNotas;

    private ObservableList<Usuario> listaEstudiantes;
    private ObservableList<Nota> listaNotasEstudianteActual; // Notas del estudiante seleccionado

    private Usuario estudianteSeleccionado; // Almacena el estudiante que el profesor ha seleccionado

 // En ProfesoresController.java

    @FXML
    public void initialize() {
        // 1. Inicializar las ObservableLists PRIMERO
        listaEstudiantes = FXCollections.observableArrayList();
        listaNotasEstudianteActual = FXCollections.observableArrayList();

        // 2. Configurar las Columnas de la tabla de estudiantes
        colIdEstudianteProfesor.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreEstudianteProfesor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreoEstudianteProfesor.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRolEstudianteProfesor.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // 3. Asignar la ObservableList a la tabla de estudiantes
        tableEstudiantesProfesor.setItems(listaEstudiantes);
        cargarEstudiantes(); // Carga inicial de estudiantes

        // 4. Configurar las Columnas de la tabla de notas
        colMateriaNotas.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colValorNotas.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // 5. Asignar la ObservableList a la tabla de notas
        tableNotas.setItems(listaNotasEstudianteActual); // <-- ¡Asegúrate que esto se ejecuta!

        // 6. Configurar los Listeners (que pueden operar sobre las listas ya inicializadas)
        // Listener para la selección de estudiantes en la tabla
        tableEstudiantesProfesor.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            estudianteSeleccionado = newSelection;
            if (estudianteSeleccionado != null) {
                txtIdEstudianteNotas.setText(String.valueOf(estudianteSeleccionado.getId()));
                txtNombreEstudianteNotas.setText(estudianteSeleccionado.getNombre());
                cargarNotasDelEstudianteSeleccionado(); // Carga las notas de este estudiante
            } else {
                txtIdEstudianteNotas.clear();
                txtNombreEstudianteNotas.clear();
                listaNotasEstudianteActual.clear(); // ¡Importante limpiar la lista, no setear a null!
            }
        });

        // Listener para la selección de notas en la tabla de notas
        tableNotas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMateria.setText(newSelection.getMateria());
                txtNota.setText(String.valueOf(newSelection.getValor()));
            } else {
                txtMateria.clear();
                txtNota.clear();
            }
        });
    

        // Inicialización de la tabla de notas (para el estudiante seleccionado)
        colMateriaNotas.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colValorNotas.setCellValueFactory(new PropertyValueFactory<>("valor"));
        listaNotasEstudianteActual = FXCollections.observableArrayList();
        tableNotas.setItems(listaNotasEstudianteActual);

        // Listener para la selección de notas en la tabla de notas (para editar/eliminar)
        tableNotas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMateria.setText(newSelection.getMateria());
                txtNota.setText(String.valueOf(newSelection.getValor()));
            } else {
                txtMateria.clear();
                txtNota.clear();
            }
        });
    }

    private void cargarEstudiantes() {
        listaEstudiantes.setAll(DataSource.getUsuariosPorRol("estudiante")); // Asumiendo que tienes este método en DataSource
        // O si no, carga todos los usuarios y filtra por rol:
        // listaEstudiantes.setAll(DataSource.getUsuarios().filtered(u -> u.getRol().equals("estudiante")));
    }

    @FXML
    public void handleBuscarEstudiante() {
        String textoBusqueda = txtBuscarEstudiante.getText().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            cargarEstudiantes(); // Si la búsqueda está vacía, mostrar todos los estudiantes
        } else {
            ObservableList<Usuario> resultados = FXCollections.observableArrayList();
            for (Usuario u : DataSource.getUsuariosPorRol("estudiante")) { // O DataSource.getUsuarios()
                if (u.getNombre().toLowerCase().contains(textoBusqueda)) {
                    resultados.add(u);
                }
            }
            tableEstudiantesProfesor.setItems(resultados);
        }
    }

    @FXML
    public void handleSeleccionarEstudianteParaNotas() {
        // La lógica de selección ya se maneja en el listener de tableEstudiantesProfesor
        // Este botón simplemente asegura que el profesor intende seleccionar para ver las notas
        if (estudianteSeleccionado == null) {
            showAlert("Error", "Por favor, selecciona un estudiante de la tabla para gestionar sus notas.");
        } else {
            showAlert("Estudiante Seleccionado", "Gestionando notas de " + estudianteSeleccionado.getNombre());
            // No es necesario hacer nada más aquí, el listener ya lo hizo
        }
    }


    // Métodos para la gestión de notas (adaptados de NotasController)
    private void cargarNotasDelEstudianteSeleccionado() {
        if (estudianteSeleccionado != null) {
            listaNotasEstudianteActual.setAll(DataSource.getNotasByEstudianteId(estudianteSeleccionado.getId()));
        } else {
            listaNotasEstudianteActual.clear();
        }
    }

    @FXML
    public void handleAgregarNota() {
        if (estudianteSeleccionado == null) {
            showAlert("Error", "Por favor, selecciona un estudiante para agregarle una nota.");
            return;
        }

        try {
            String materia = txtMateria.getText();
            double notaValor = Double.parseDouble(txtNota.getText());

            if (materia.isEmpty()) {
                showAlert("Campos vacíos", "Por favor, ingresa la materia.");
                return;
            }

            Nota nuevaNota = new Nota(estudianteSeleccionado.getId(), estudianteSeleccionado.getNombre(), materia, notaValor);
            DataSource.addNota(nuevaNota); // Usar tu DataSource

            cargarNotasDelEstudianteSeleccionado(); // Recargar la tabla de notas
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
    public void handleEditarNota() {
        Nota notaSeleccionada = tableNotas.getSelectionModel().getSelectedItem();
        if (notaSeleccionada != null) {
            try {
                notaSeleccionada.setMateria(txtMateria.getText());
                notaSeleccionada.setValor(Double.parseDouble(txtNota.getText()));
                DataSource.updateNota(notaSeleccionada); // Usar tu DataSource

                tableNotas.refresh(); // Refrescar la vista de la tabla
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
    public void handleEliminarNota() {
        Nota notaSeleccionada = tableNotas.getSelectionModel().getSelectedItem();
        if (notaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar Nota");
            alert.setContentText("¿Estás seguro de que quieres eliminar esta nota?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                DataSource.deleteNota(notaSeleccionada.getId()); // Usar tu DataSource
                listaNotasEstudianteActual.remove(notaSeleccionada); // Eliminar de la ObservableList
                showAlert("Éxito", "Nota eliminada correctamente.");
            }
        } else {
            showAlert("Ninguna nota seleccionada", "Por favor, selecciona una nota de la tabla para eliminar.");
        }
    }

    @FXML
    public void handleVolverAlLogin() {
        try {
            Stage stage = (Stage) tableEstudiantesProfesor.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/application/View/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo volver a la vista de login.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}