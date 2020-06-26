package eu.iteije.kitpvp.runnables;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.data.MySQL;
import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.pluginutils.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeaderboardUpdater {

    private KitPvP instance;

    public LeaderboardUpdater(KitPvP instance) {
        this.instance = instance;
    }

    @Deprecated
    public Integer start() {
        return instance.getServer().getScheduler().scheduleAsyncRepeatingTask(instance, () -> {
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

                    UserCache.leaderboard = leaderboard;

                    statement.close();
                    resultSet.close();
                } catch (SQLException exception) {
                    // Send failed message
                    Message.sendToConsole(Message.get("mysql_no_connection"), true);
                    // Print stack trace to track errors
                    exception.printStackTrace();
                }

            });
        }, 30 * 20L, 20L);
    }
}
