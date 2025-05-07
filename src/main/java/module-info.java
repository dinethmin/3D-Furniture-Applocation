module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.fxyz3d.core;
    requires org.fxyz3d.importers;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}