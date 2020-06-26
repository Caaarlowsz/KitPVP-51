package eu.iteije.kitpvp.data;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("DuplicatedCode")
public class DataHandler {

    private static DataHandler dataHandler = new DataHandler();
    private KitPvP instance = KitPvP.getInstance();

    // Load player (and add uuid if it doesn't exists)
    public void loadPlayer(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                // Try receiving player data from database
                PreparedStatement statement = MySQL.connection.prepareStatement("SELECT uuid FROM players WHERE uuid=?");
                statement.setString(1, uuid.toString());

                // Execute PreparedStatement query and save results
                ResultSet resultSet = statement.executeQuery();

                // If resultSet.next is true, player exists (save data to cache class), otherwise create it
                if (resultSet.next()) {
                    // Save playerdata (kills/deaths)
                    DataHandler.getHandler().saveCurrentKills(uuid);
                    DataHandler.getHandler().saveCurrentDeaths(uuid);
                } else {
                    // Create new player with default data
                    PreparedStatement insertPlayer = MySQL.connection.prepareStatement("INSERT INTO players (uuid, username, kills, deaths) VALUES (?,?,?,?)");
                    insertPlayer.setString(1, uuid.toString());
                    insertPlayer.setString(2, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                    insertPlayer.setInt(3, 0);
                    insertPlayer.setInt(4, 0);

                    // Execute query
                    insertPlayer.executeUpdate();

                    // Save playerdata (kills/deaths)
                    UserCache.updateKills(uuid, 0);
                    UserCache.updateDeaths(uuid, 0);

                    // I remember getting comments about not closing prepared statements
                    insertPlayer.close();
                }
                statement.close();
                resultSet.close();
            } catch (SQLException exception) {
                // Send failed message
                Message.sendToConsole(Message.get("mysql_no_connection"), true);
                // Print stack trace to track errors
                exception.printStackTrace();
            }

        });
    }

    // Save current kills to UserCache class
    public void saveCurrentKills(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                // Try receiving kill data
                PreparedStatement statement = MySQL.connection.prepareStatement("SELECT kills FROM players WHERE uuid=?");
                statement.setString(1, uuid.toString());

                // Execute query and save results
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Save amount of kills
                    int kills = resultSet.getInt(1);
                    UserCache.updateKills(uuid, kills);
                }
                statement.close();
            } catch (SQLException exception) {
                // Send failed message
                Message.sendToConsole(Message.get("mysql_no_connection"), true);
                // Print stack trace to track errors
                exception.printStackTrace();
            }
        });
    }

    // Save current deaths to UserCache class
    public void saveCurrentDeaths(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                // Try receiving death data
                PreparedStatement statement = MySQL.connection.prepareStatement("SELECT deaths FROM players WHERE uuid=?");
                statement.setString(1, uuid.toString());

                // Execute query and save results
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Save amount of deaths
                    int deaths = resultSet.getInt(1);
                    UserCache.updateDeaths(uuid, deaths);
                }
                statement.close();
            } catch (SQLException exception) {
                // Send failed message
                Message.sendToConsole(Message.get("mysql_no_connection"), true);
                // Print stack trace to track errors
                exception.printStackTrace();
            }
        });
    }




    // Add a death for a specific player
    public void addDeath(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
                try {
                    // Try receiving current deaths from database
                    PreparedStatement currentDeathsStatement = MySQL.connection.prepareStatement("SELECT deaths FROM players WHERE uuid=?");
                    currentDeathsStatement.setString(1, uuid.toString());

                    // Execute PreparedStatement and save results
                    ResultSet resultSet = currentDeathsStatement.executeQuery();

                    // If resultSet has content, update deaths
                    if (resultSet.next()) {
                        // Define current (old) amount of deaths
                        int currentDeaths = resultSet.getInt(1);
                        // Add kill to database
                        PreparedStatement insertDeath = MySQL.connection.prepareStatement("UPDATE players SET deaths=? WHERE uuid=?");
                        insertDeath.setInt(1, currentDeaths + 1);
                        insertDeath.setString(2, uuid.toString());

                        // Execute query
                        insertDeath.executeUpdate();
                    }

                } catch (SQLException exception) {
                    // Send failed message
                    Message.sendToConsole(Message.get("mysql_no_connection"), true);
                    // Print stack trace to track errors
                    exception.printStackTrace();
                }
        });
    }

    // Add a kill for a specific player
    public void addKill(UUID uuid) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
                try {
                    // Try receiving current kills from database
                    PreparedStatement currentKillsStatement = MySQL.connection.prepareStatement("SELECT kills FROM players WHERE uuid=?");
                    currentKillsStatement.setString(1, uuid.toString());

                    // Execute PreparedStatement and save results
                    ResultSet resultSet = currentKillsStatement.executeQuery();

                    // If resultSet has content, update kills
                    if (resultSet.next()) {
                        // Define current (old) amount of kills
                        int currentKills = resultSet.getInt(1);
                        // Add kill to database
                        PreparedStatement insertKill = MySQL.connection.prepareStatement("UPDATE players SET kills=? WHERE uuid=?");
                        insertKill.setInt(1, currentKills + 1);
                        insertKill.setString(2, uuid.toString());

                        // Execute query
                        insertKill.executeUpdate();
                    }


                } catch (SQLException exception) {
                    // Send failed message
                    Message.sendToConsole(Message.get("mysql_no_connection"), true);
                    // Print stack trace to track errors
                    exception.printStackTrace();
                }
        });
    }

    public static DataHandler getHandler() {
        return dataHandler;
    }
}
