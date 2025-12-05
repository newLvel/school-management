module com.example.schoolmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.schoolmanagement to javafx.fxml;
    opens com.example.schoolmanagement.controllers to javafx.fxml;
    opens com.example.schoolmanagement.models to javafx.base;
    exports com.example.schoolmanagement;
}