package application.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nota {
    private final IntegerProperty id; // ID único para la nota (si se usa un DataSource)
    private final IntegerProperty idEstudiante;
    private final StringProperty nombreEstudiante; // <--- AGREGAR ESTA PROPIEDAD
    private final StringProperty materia;
    private final DoubleProperty valor;

    // Constructor para cuando se crea una nueva nota
    public Nota(int idEstudiante, String nombreEstudiante, String materia, double valor) {
        this.id = new SimpleIntegerProperty(0); // El ID real lo asignará DataSource
        this.idEstudiante = new SimpleIntegerProperty(idEstudiante);
        this.nombreEstudiante = new SimpleStringProperty(nombreEstudiante);
        this.materia = new SimpleStringProperty(materia);
        this.valor = new SimpleDoubleProperty(valor);
    }

    // Constructor para cuando se carga una nota de la "base de datos" (con ID ya asignado)
    public Nota(int id, int idEstudiante, String nombreEstudiante, String materia, double valor) {
        this.id = new SimpleIntegerProperty(id);
        this.idEstudiante = new SimpleIntegerProperty(idEstudiante);
        this.nombreEstudiante = new SimpleStringProperty(nombreEstudiante);
        this.materia = new SimpleStringProperty(materia);
        this.valor = new SimpleDoubleProperty(valor);
    }

    // Getters para las propiedades de JavaFX
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty idEstudianteProperty() { return idEstudiante; }
    public StringProperty nombreEstudianteProperty() { return nombreEstudiante; } // Getter de la propiedad
    public StringProperty materiaProperty() { return materia; }
    public DoubleProperty valorProperty() { return valor; }

    // Getters para los valores primitivos (más comunes para acceder)
    public int getId() { return id.get(); }
    public int getIdEstudiante() { return idEstudiante.get(); }
    public String getNombreEstudiante() { return nombreEstudiante.get(); } // Getter del valor
    public String getMateria() { return materia.get(); }
    public double getValor() { return valor.get(); }

    // Setters para los valores primitivos (para modificar la nota)
    public void setId(int id) { this.id.set(id); }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante.set(idEstudiante); }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante.set(nombreEstudiante); }
    public void setMateria(String materia) { this.materia.set(materia); }
    public void setValor(double valor) { this.valor.set(valor); }
}