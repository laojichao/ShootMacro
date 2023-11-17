module com.la0i6.shootmacro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.github.kwhat.jnativehook;
    requires fastjson;
    requires retrofit2;
    requires okhttp;
    requires io.reactivex.rxjava2;
    requires java.management;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    opens com.la0i6.shootmacro to javafx.fxml;
    exports com.la0i6.shootmacro;
}