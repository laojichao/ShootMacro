module com.la0i6.shootmacro {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens com.la0i6.shootmacro to javafx.fxml;
    exports com.la0i6.shootmacro;
}