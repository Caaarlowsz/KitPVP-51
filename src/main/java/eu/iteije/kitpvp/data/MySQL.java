package eu.iteije.kitpvp.data;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static MySQL database = new MySQL();
    private Connection connection;

    // Instance of KitPvP
    private KitPvP instance = KitPvP.getInstance();

    // Instance of ConfigFile
    private ConfigFile configFile = new ConfigFile(instance, false);

    // Open/start connection method
    public synchronized void openConnection() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            // Try establishing a connection
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                        configFile.get().getString("database.host") + ":" +
                        configFile.get().get("database.port") + "/" +
                        configFile.get().getString("database.database") + "?autoReconnect=false",
                        configFile.get().getString("database.username"),
                        configFile.get().getString("database.password"));

                if (getCurrentConnection() != null && !getCurrentConnection().isClosed()) {
                    // Send success message
                    Message.sendToConsole(Message.get("mysql_connection_success"), true);
                }

            } catch (Exception exception) { // there are way too many types of exception which could occur here
                // Send error message
                Message.sendToConsole(Message.get("mysql_connection_failed"), true);
                // Print stack trace
                exception.printStackTrace();
            }
        });
    }

    // Close connection method
    public synchronized void closeConnection() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                // Check whether the connection is already closed or not
                if ((!connection.isClosed()) || (connection != null)) {
                    // Close connection
                    connection.close();
                }
            } catch (SQLException exception) {
                // Print stack trace
                exception.printStackTrace();
            }
        });
    }

    public synchronized void checkTable() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                if (getCurrentConnection() != null && !getCurrentConnection().isClosed()) {
                    // Create new table if it does not exists
                    // Table details: uuid - varchar - 36, kills - int, deaths - int
                    getCurrentConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS players(uuid varchar(36), kills int, deaths int)");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public Connection getCurrentConnection() {
        // Try making up updated location
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                    configFile.get().getString("database.host") + ":" +
                    configFile.get().get("database.port") + "/" +
                    configFile.get().getString("database.database") + "?autoReconnect=false",
                    configFile.get().getString("database.username"),
                    configFile.get().getString("database.password"));
        } catch (Exception exception) { // again, way too many types of exception which could occur here
            // Print stack trace
            exception.printStackTrace();
        }
        // Return current connection
        return connection;
    }

    // Get instance of MySQL
    public static MySQL getDatabase() {
        return database;
    }



}