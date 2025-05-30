package application.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

import application.Model.Estudiante;
import application.Model.Nota;
import application.Model.Usuario;

public class DataSource {

    // Listas observables para las colecciones de datos
    private static ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private static ObservableList<Nota> notas = FXCollections.observableArrayList();
    private static ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(); // Necesitaremos esta

    // Para gestionar IDs automáticos
    private static int nextUsuarioId = 1;
    private static int nextNotaId = 1;
    private static int nextEstudianteId = 1;

    // Mapa para un acceso rápido a usuarios por credenciales
    private static Map<String, Usuario> usuariosPorCredenciales = new HashMap<>();

    static {
        // Cargar algunos datos de prueba al iniciar la aplicación
        // Usuarios: admin, profesor, estudiante
        Usuario admin = new Usuario(nextUsuarioId++, "Admin", "admin@example.com", "admin", "admin123");
        Usuario profesor = new Usuario(nextUsuarioId++, "Profesor Demo", "profesor@example.com", "profesor", "prof123");
        Usuario estudiante1 = new Usuario(nextUsuarioId++, "Juan Perez", "juan@example.com", "estudiante", "est123");
        Usuario estudiante2 = new Usuario(nextUsuarioId++, "Maria Lopez", "maria@example.com", "estudiante", "est123");

        usuarios.addAll(admin, profesor, estudiante1, estudiante2);

        usuariosPorCredenciales.put("admin", admin);
        usuariosPorCredenciales.put("profesor", profesor);
        usuariosPorCredenciales.put("estudiante", estudiante1); // Usamos al primer estudiante para la demo de login

        // Estudiantes (solo los que son rol "estudiante")
        estudiantes.add(new Estudiante(estudiante1.getId(), estudiante1.getNombre(), estudiante1.getCorreo()));
        estudiantes.add(new Estudiante(estudiante2.getId(), estudiante2.getNombre(), estudiante2.getCorreo()));

        // Notas de ejemplo (asumiendo que sabemos los IDs de los estudiantes)
        notas.add(new Nota(nextNotaId++, estudiante1.getId(), estudiante1.getNombre(), "Matemáticas", 4.5));
        notas.add(new Nota(nextNotaId++, estudiante1.getId(), estudiante1.getNombre(), "Lengua", 3.8));
        notas.add(new Nota(nextNotaId++, estudiante2.getId(), estudiante2.getNombre(), "Ciencias", 4.0));
        notas.add(new Nota(nextNotaId++, estudiante2.getId(), estudiante2.getNombre(), "Matemáticas", 3.2));
    }

    public static ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static ObservableList<Nota> getNotas() {
        return notas;
    }

    public static ObservableList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public static Usuario autenticarUsuario(String username, String password) {
        Usuario user = usuariosPorCredenciales.get(username);
        if (user != null && user.getContrasena().equals(password)) { // Asumiendo que Usuario tiene getContrasena()
            return user;
        }
        return null;
    }

    // Métodos CRUD para Usuarios
    public static void addUsuario(Usuario user) {
        user.setId(nextUsuarioId++);
        usuarios.add(user);
        usuariosPorCredenciales.put(user.getNombre().toLowerCase().replaceAll("\\s", ""), user); // Usa el nombre como clave por ahora
        System.out.println("Usuario añadido y credenciales actualizadas: " + user.getNombre() + ", Rol: " + user.getRol());
    }

    public static void updateUsuario(Usuario updatedUser) {
        for (int i = 0; i < usuarios.size(); i++) { //
            if (usuarios.get(i).getId() == updatedUser.getId()) { //
                usuarios.set(i, updatedUser); 
                usuariosPorCredenciales.remove(usuarios.get(i).getNombre().toLowerCase().replaceAll("\\s", "")); 
                usuariosPorCredenciales.put(updatedUser.getNombre().toLowerCase().replaceAll("\\s", ""), updatedUser); 
                System.out.println("Usuario modificado: " + updatedUser.getNombre()); //
                return; //
            }
        }
    }

    public static void deleteUsuario(int userId) {
        Usuario userToRemove = null;
        for (Usuario u : usuarios) {
            if (u.getId() == userId) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            usuarios.remove(userToRemove);
            usuariosPorCredenciales.remove(userToRemove.getNombre().toLowerCase().replaceAll("\\s", ""));
        }
        
    }

    // Métodos CRUD para Notas    
    public static void addNota(Nota nota) {
        nota.setId(nextNotaId++); // Asumiendo que Nota tendrá un ID propio
        notas.add(nota);
    }

    public static void updateNota(Nota updatedNota) {
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getId() == updatedNota.getId()) { // Asumiendo que Nota tendrá un ID propio
                notas.set(i, updatedNota);
                return;
            }
        }
    }

    public static void deleteNota(int notaId) { 
        notas.removeIf(nota -> nota.getId() == notaId);
        System.out.println("Nota con ID " + notaId + " eliminada."); 
    }

    public static Usuario getUsuarioById(int id) {
        for (Usuario user : usuarios) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
    // Métodos para obtener notas de un estudiante específico
    public static ObservableList<Nota> getNotasByEstudianteId(int estudianteId) {
        ObservableList<Nota> notasFiltradas = FXCollections.observableArrayList();
        for (Nota nota : notas) {
            if (nota.getIdEstudiante() == estudianteId) {
                notasFiltradas.add(nota);
            }
        }
        return notasFiltradas;
    }
    
    public static Usuario authenticateUser(String username, String password) {
        // La clave en el mapa es el nombre de usuario en minúsculas y sin espacios
        Usuario user = usuariosPorCredenciales.get(username.toLowerCase().replaceAll("\\s", ""));
        if (user != null && user.getContrasena().equals(password)) {
            return user;
        }
        return null;
    }
    
    public static ObservableList<Usuario> getUsuariosPorRol(String rol) {
        ObservableList<Usuario> usuariosFiltrados = FXCollections.observableArrayList();
        for (Usuario user : usuarios) { // Asumiendo que 'usuarios' es tu ObservableList<Usuario> global en DataSource
            if (user.getRol().equalsIgnoreCase(rol)) { // Compara el rol ignorando mayúsculas/minúsculas
                usuariosFiltrados.add(user);
            }
        }
        return usuariosFiltrados;
    }
    
    
    
   
    
    
}