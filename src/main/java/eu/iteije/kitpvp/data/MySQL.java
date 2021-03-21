package eu.iteije.kitpvp.data;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static MySQL database = new MySQL();
    // By making the connection public for the entire plugin, it is not necessary to create a new connection every single time something needs access to the database
    public static Connection connection;

    // Instance of KitPvP
    private KitPvP instance = KitPvP.getInstance();

    // Instance of ConfigFile
    private PluginFile configFile = KitPvP.getInstance().getConfigFile();

    // Open/start connection method
    public synchronized void openConnection() {
        if (configFile.get().getString("database.host").equals("")) {
            // Well, please don't broadcast random hardcoded messages
        }

        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            // Try establishing a connection
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                configFile.get().getString("database.host") + ":" +
                                configFile.get().get("database.port") + "/" +
                                configFile.get().getString("database.database") + "?autoReconnect=false",
                        configFile.get().getString("database.username"),
                        configFile.get().getString("database.password"));

                if (!checkTable()) throw new SQLException();

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
    public synchronized void closeConnection(boolean sync) {
        if (!sync) {
            instance.getServer().getScheduler().runTaskAsynchronously(instance, (Runnable) this::closeConnection);
            return;
        }
        closeConnection();
    }

    private synchronized void closeConnection() {
        try {
            // Check whether the connection is already closed or not
            if ((!connection.isClosed()) || (connection != null)) {
                // Close connection
                connection.close();
                Message.sendToConsole("&cDatabase connection has been closed", true);
            }
        } catch (SQLException exception) {
            // Print stack trace
            exception.printStackTrace();
        }
    }

    public synchronized boolean checkTable() {
        try {
            if (getCurrentConnection() != null && !getCurrentConnection().isClosed()) {
                // Create new table if it does not exists
                // Table details: uuid - varchar - 36, kills - int, deaths - int
                getCurrentConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS players(uuid varchar(36), username varchar(36), kills int, deaths int)");
            }
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
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
