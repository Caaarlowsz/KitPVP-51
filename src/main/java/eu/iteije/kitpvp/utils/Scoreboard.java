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
        deaths = deaths == 0 ? 1 : deaths;

        // Calculate K/D ratio (WARNING: this can output unlimited digits)
        String killDeathRatio = String.format(Locale.US, "%.2f", (double) kills / deaths);


        // Create new Objective, display name is defined later
        Objective objective = scoreboard.registerNewObjective("KitPvP", "");
        // Make it a sidebar scoreboard
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        // Set display name
        objective.setDisplayName(TransferMessage.replaceColorCodes("       &5&lVioletPvP      "));

        List<Score> scores = new ArrayList<>();

        // Scores
        // Kills score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&fKills: &d" + kills)));
        // Deaths score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&fDeaths: &d" + deaths)));
        // Kill/death ratio score
        scores.add(objective.getScore(TransferMessage.replaceColorCodes("&fK/D ratio: &d" + killDeathRatio)));

        scores.add(objective.getScore("§1"));
        scores.add(objective.getScore("&dplay.violetpvp.net"));


        // Set all scores
        for (int i = 0; i <= ((scores.size()) - 1); i++) {
            scores.get(i).setScore((scores.size()) - i - 1);
        }

        // Set scoreboard
        player.setScoreboard(scoreboard);
    }

}
