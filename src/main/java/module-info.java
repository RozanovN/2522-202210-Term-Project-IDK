module ca.bcit.comp2522.termproject.idk {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens ca.bcit.comp2522.termproject.idk to javafx.fxml;
    exports ca.bcit.comp2522.termproject.idk;
}