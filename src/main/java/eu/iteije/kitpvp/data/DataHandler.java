package eu.iteije.kitpvp.data;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DataHandler {

    private static DataHandler dataHandler = new DataHandler();
    private KitPvP instance = KitPvP.getInstance();

    // Load player (and add uuid if it doesn't exists)
    public void loadPlayer(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, new Runnable() {
            @Override
            public void run() {
                try {
                    // Try receiving player data from database
                    PreparedStatement statement = MySQL.getDatabase().getCurrentConnection().prepareStatement("SELECT uuid FROM players WHERE uuid=?");
                    statement.setString(1, uuid.toString());

                    // Execute PreparedStatement query and save results
                    ResultSet resultSet = statement.executeQuery();

                    // If resultSet.next is true, player exists (save data to cache class), otherwise create it
                    if (resultSet.next()) {
                        // Get player data and save it
                    } else {
                        // Create new player with default data
                        PreparedStatement insertPlayer = MySQL.getDatabase().getCurrentConnection().prepareStatement("INSERT INTO players (uuid, kills, deaths) VALUES (?,?,?)");
                        insertPlayer.setString(1, uuid.toString());
                        insertPlayer.setInt(2, 0);
                        insertPlayer.setInt(3, 0);

                        // Execute query
                        insertPlayer.executeUpdate();
                    }
                    resultSet.close();
                } catch (SQLException exception) {
                    // Send failed message
                    Message.sendToConsole(Message.get("mysql_no_connection"), true);
                    // Print stack trace to track errors
                    exception.printStackTrace();
                }

            }
        });
    }

    public static DataHandler getHandler() {
        return dataHandler;
    }
}
