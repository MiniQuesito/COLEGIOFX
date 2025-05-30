package application.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import application.Model.Usuario;
import application.Service.DataSource;

public class AgregarUsuarioDialogController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField pwdContrasena;
    @FXML
    private ComboBox<String> cmbRol;

    private Stage dialogStage;
    private Usuario usuario; // Para el caso de modificar, por ahora no se usa
    private boolean guardarClicked = false; // Para saber si se hizo clic en Guardar


    /**
     * Inicializa la clase controlador. Este método se llama automáticamente
     * después de que el archivo FXML ha sido cargado.
     */
    @FXML
    private void initialize() {
        // Puedes establecer un valor por defecto o cargar opciones para el ComboBox aquí
        // cmbRol.getSelectionModel().selectFirst(); // Selecciona el primer elemento por defecto
    }

    /**
     * Establece el stage de este diálogo.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Establece el usuario a editar en el diálogo.
     * @param usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; //
        if (usuario != null) { //
            // Descomenta y asegúrate de que los campos se llenen aquí
            txtNombre.setText(usuario.getNombre()); //
            txtCorreo.setText(usuario.getCorreo()); //
            pwdContrasena.setText(usuario.getContrasena()); //
            cmbRol.getSelectionModel().select(usuario.getRol()); //
            // Si tienes una bandera isModifying, actívala aquí:
            // isModifying = true;
        }
    }

    /**
     * Retorna true si el usuario hizo clic en Guardar, false de lo contrario.
     * @return
     */
    public boolean isGuardarClicked() {
        return guardarClicked;
    }

    /**
     * Llamado cuando el usuario hace clic en Guardar.
     */
    @FXML
    private void handleGuardarUsuario() {
        if (isInputValid()) { // Debes tener este método para validar los campos
            if (this.usuario != null && this.usuario.getId() != 0) { // Modo Modificar: Si el usuario ya tiene un ID asignado
                // Actualizar el usuario existente
                this.usuario.setNombre(txtNombre.getText());
                this.usuario.setCorreo(txtCorreo.getText());
                this.usuario.setContrasena(pwdContrasena.getText());
                this.usuario.setRol(cmbRol.getValue());

                application.Service.DataSource.updateUsuario(this.usuario); // Llama al método de actualización en DataSource
            } else {
                // Modo Agregar: Crear un nuevo usuario
                Usuario nuevoUsuario = new Usuario(
                    0, // El ID se asignará en DataSource.addUsuario
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    cmbRol.getValue(),
                    pwdContrasena.getText()
                );
                application.Service.DataSource.addUsuario(nuevoUsuario); // Llama al método de agregar en DataSource
            }
            guardarClicked = true;
            dialogStage.close();
        }
     }

    /**
     * Llamado cuando el usuario hace clic en Cancelar.
     */
    @FXML
    private void handleCancelar() {
        dialogStage.close(); // Cierra el diálogo
    }

    /**
     * Valida la entrada del usuario en los campos de texto.
     * @return true si la entrada es válida, false en caso contrario.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtNombre.getText() == null || txtNombre.getText().length() == 0) {
            errorMessage += "¡No se ha introducido un nombre válido!\n";
        }
        if (txtCorreo.getText() == null || txtCorreo.getText().length() == 0) {
            errorMessage += "¡No se ha introducido un correo válido!\n";
        }
        if (pwdContrasena.getText() == null || pwdContrasena.getText().length() == 0) {
            errorMessage += "¡No se ha introducido una contraseña válida!\n";
        }
        if (cmbRol.getValue() == null || cmbRol.getValue().length() == 0) {
            errorMessage += "¡No se ha seleccionado un rol válido!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrar el mensaje de error.
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, corrige los campos inválidos");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}