package application.Model;

import javafx.beans.property.*;

public class Estudiante {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty correo;

    public Estudiante(int id, String nombre, String correo) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.correo = new SimpleStringProperty(correo);
    }

    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getCorreo() { return correo.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setCorreo(String correo) { this.correo.set(correo); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty correoProperty() { return correo; }
}
