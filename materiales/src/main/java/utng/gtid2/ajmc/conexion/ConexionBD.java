package utng.gtid2.ajmc.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;

    private Connection conexion;

    private ConexionBD() {

        try {

            String url = "jdbc:postgresql://localhost:5432/materialdb";

            conexion = DriverManager.getConnection(
                    url,
                    "instrumento",
                    "12345"
            );

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static ConexionBD getInstancia() {

        if (instancia == null) {

            instancia = new ConexionBD();

        }

        return instancia;

    }

    public Connection getConexion() {

        return conexion;

    }

}