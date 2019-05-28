module novaTela.aps.cliente.Main {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires kotlin.stdlib;
    requires slf4j.api;

    exports aps.cliente.login;
    exports aps.cliente.chatwindow;

    opens aps.cliente.login;
    opens aps.cliente.chatwindow;
    opens aps.globalClass.traynotification.notification;
}