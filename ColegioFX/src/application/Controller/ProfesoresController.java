package application.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.Model.Usuario;



public class ProfesoresController {
	
    @FXML
    private TableView<Usuario> tableProfesores;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colRol;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    private final ObservableList<Usuario> listaProfesores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        listaProfesores.addAll(
            new Usuario(1, "Carlos Díaz", "carlos@example.com", "profesor"),
            new Usuario(2, "Lucía Torres", "lucia@example.com", "profesor")
        );

        tableProfesores.setItems(listaProfesores);
    }

}
