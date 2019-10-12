package eu.iteije.kitpvp.utils;

import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Scoreboard {

    public static void load(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        // Get player data
        Integer kills = UserCache.getKills(player.getUniqueId());
        Integer deaths = UserCache.getDeaths(player.getUniqueId());

        // Calculate K/D ratio (WARNING: this can output unlimited digits)
        String killDeathRatio;

        try {
            // In case a integer is 0 (it is not possible to divide by 0, remember?)
            if (kills == 0 && deaths == 0) {
                killDeathRatio = String.valueOf(0.00);
            } else if (kills != 0 && deaths == 0) {
                killDeathRatio = String.valueOf(kills);
            } else if (kills == 0 && deaths != 0) {
                killDeathRatio = String.valueOf(0);
            } else {
                killDeathRatio = String.format(Locale.US, "%.2f", (double) kills / deaths);
            }
        } catch (NullPointerException exception) {
            killDeathRatio = "Log opnieuw in";
        }


        // Create new Objective, display name is defined later
        Objective objective = scoreboard.registerNewObjective("KitPvP", "", "");
        // Make it a sidebar scoreboard
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        // Set display name
        objective.setDisplayName(TransferMessage.replaceColorCodes("       &c&lKitPvP      "));

        List<Score> scores = new ArrayList<>();

        // Scores
        // Kills score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&7Kills:"))); // Title > Kills
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&f" + kills + "&1"))); // Content > Kills
        // Blank line score
        scores.add(objective.getScore("§1")); // Blank line
        // Deaths score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&7Deaths:"))); // Title > Deaths
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&f" + deaths + "&2"))); // Content > Deaths
        // Blank line score
        scores.add(objective.getScore("§2")); // Blank line
        // Kill/death ratio score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&7K/D:"))); // Title > KD ratio
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&f" + killDeathRatio + "&3"))); // Content > KD ratio


        // Set all scores
        for (int i = 0; i <= ((scores.size()) - 1); i++) {
            scores.get(i).setScore((scores.size()) - i - 1);
        }

        // Set scoreboard
        player.setScoreboard(scoreboard);
    }

}
