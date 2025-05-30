
module ColegioFX { // El nombre de tu módulo
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // Podría ser necesario para algunas cosas de UI

    // Exporta el paquete donde está tu clase Main
    exports application;
    // Exporta el paquete de tus controladores para que FXML pueda acceder a ellos
    exports application.Controller;
    // Exporta el paquete de tus modelos si las clases del modelo son referenciadas en FXML (ej. TableView<Nota>)
    exports application.Model;

    // Abrir estos paquetes a javafx.fxml para permitir la inyección de @FXML
    opens application.Controller to javafx.fxml;
    opens application.Model to javafx.fxml; // Necesario si tus modelos son usados en TableView cellValueFactory
    opens application to javafx.fxml; // Puede ser necesario si Main o cosas de alto nivel necesitan inyección
}