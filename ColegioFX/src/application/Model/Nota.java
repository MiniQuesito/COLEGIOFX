package application.Model;

public class Nota {

	    private int idEstudiante;
	    private String nombreEstudiante;
	    private String materia;
	    private double valor;

	    public Nota(int idEstudiante, String nombreEstudiante, String materia, double valor) {
	        this.idEstudiante = idEstudiante;
	        this.nombreEstudiante = nombreEstudiante;
	        this.materia = materia;
	        this.valor = valor;
	    }

	    public int getIdEstudiante() {
	        return idEstudiante;
	    }

	    public void setIdEstudiante(int idEstudiante) {
	        this.idEstudiante = idEstudiante;
	    }

	    public String getNombreEstudiante() {
	        return nombreEstudiante;
	    }

	    public void setNombreEstudiante(String nombreEstudiante) {
	        this.nombreEstudiante = nombreEstudiante;
	    }

	    public String getMateria() {
	        return materia;
	    }

	    public void setMateria(String materia) {
	        this.materia = materia;
	    }

	    public double getValor() {
	        return valor;
	    }

	    public void setValor(double valor) {
	        this.valor = valor;
	    }

}
