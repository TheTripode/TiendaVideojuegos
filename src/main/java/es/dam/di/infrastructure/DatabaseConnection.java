package es.dam.di.infrastructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseConnection {

    private static String url;
    private static String driver;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo db.properties");
            }
            prop.load(input);
            url = prop.getProperty("db.url");
            driver = prop.getProperty("db.driver");

            // Carga la clase org.sqlite.JDBC, vamos para saber hacer conexiones de bd
            Class.forName(driver);

            // Asegura que el directorio data/ exista si se usa una ruta relativa
            if (url.startsWith("jdbc:sqlite:")) {
                String dbPath = url.replace("jdbc:sqlite:", "");
                File dbFile = new File(dbPath);
                if (dbFile.getParentFile() != null) {
                    // Si no existe crea la carpeta "data" en el disco
                    dbFile.getParentFile().mkdirs();
                }
            }

            // Inicializar las tablas si no existen
            initDatabase();

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private static void initDatabase() {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo schema.sql");
            }

            String sql = new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }
}