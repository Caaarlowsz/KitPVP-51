package eu.iteije.kitpvp.data;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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


    // I don't wanna rewrite this entire plugin just because I want a solid leaderboard
    // It's the most ugly and most unsafe code I've ever seen but that doesn't matter today
    // Load player (and add uuid if it doesn't exists)
    public void sendTop(CommandSender sender) {
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        // blocking main thread yah
        AtomicBoolean done= new AtomicBoolean(false);
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                // Try receiving player data from database
                PreparedStatement statement = MySQL.connection.prepareStatement("SELECT uuid FROM players ORDER BY kills DESC LIMIT 10");

                ResultSet resultSet = statement.executeQuery();

                String previous = "";
                while (resultSet.next()) {
                    String uuid = resultSet.getString(1);
                    if (uuid.equals(previous)) break;
                    previous = uuid;

                    PreparedStatement usernameStatement = MySQL.connection.prepareStatement("SELECT username FROM players WHERE uuid=?");
                    usernameStatement.setString(1, uuid);

                    ResultSet userNameSet = usernameStatement.executeQuery();
                    String userName = "";
                    if (userNameSet.next()) {
                        userName = userNameSet.getString(1);
                    }

                    usernameStatement.close();

                    PreparedStatement killsStatement = MySQL.connection.prepareStatement("SELECT kills FROM players WHERE uuid=?");
                    killsStatement.setString(1, uuid);

                    ResultSet killsSet = killsStatement.executeQuery();
                    int kills = 0;
                    if (killsSet.next()) {
                        kills = killsSet.getInt(1);
                    }


                    leaderboard.put(userName, kills);

                    usernameStatement.close();
                    userNameSet.close();
                    killsStatement.close();
                    killsSet.close();
                }

                sendMessage(sender, "&3&lTop 10:");
                int count = 1;
                for (String user : leaderboard.keySet()) {
                    sendMessage(sender, "&b" + count + "&7 - &b" + user + " &f(" + leaderboard.get(user) + ")");
                    count++;
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

    // Why is this in the datahandler?
    private void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }








    public static DataHandler getHandler() {
        return dataHandler;
    }
}
