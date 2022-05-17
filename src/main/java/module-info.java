module com.example.komanjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.example.komanjava to javafx.fxml, google.code.gson, com.google.gson.Gson;
    exports com.example.komanjava;
    exports GUI to com.google.gson;
    opens GUI to com.google.gson;
    exports SceneManager to com.google.gson;
    opens SceneManager to com.google.gson;
}