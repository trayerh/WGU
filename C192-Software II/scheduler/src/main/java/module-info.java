module view_controller.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens view_controller.scheduler to javafx.fxml;
    opens model.scheduler to javafx.base;
    exports view_controller.scheduler;
}