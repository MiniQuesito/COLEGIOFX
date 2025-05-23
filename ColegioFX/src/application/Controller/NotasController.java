package application.Controller;

	import javafx.fxml.FXML;
	import javafx.scene.control.Button;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import application.Model.Nota;

	public class NotasController {

	    @FXML
	    private TableView<Nota> tableNotas;

	    @FXML
	    private TableColumn<Nota, Integer> colIdEstudiante;

	    @FXML
	    private TableColumn<Nota, String> colNombre;

	    @FXML
	    private TableColumn<Nota, String> colMateria;

	    @FXML
	    private TableColumn<Nota, Double> colNota;

	    @FXML
	    private Button btnAgregar;

	    @FXML
	    private Button btnEditar;

	    @FXML
	    private Button btnEliminar;

	    private final ObservableList<Nota> listaNotas = FXCollections.observableArrayList();

	    @FXML
	    public void initialize() {
	        colIdEstudiante.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
	        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
	        colMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
	        colNota.setCellValueFactory(new PropertyValueFactory<>("valor"));

	        listaNotas.addAll(
	            new Nota(1, "Juan Pérez", "Matemáticas", 4.5),
	            new Nota(2, "Ana López", "Historia", 3.8)
	        );

	        tableNotas.setItems(listaNotas);
	    }
	}


