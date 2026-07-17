module utng.gtid2.ajmc {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens utng.gtid2.ajmc to javafx.fxml;
    opens utng.gtid2.ajmc.controlador to javafx.fxml;
    opens utng.gtid2.ajmc.modelo to javafx.base;

    exports utng.gtid2.ajmc;
    exports utng.gtid2.ajmc.controlador;
}