package eu.iteije.kitpvp;

import eu.iteije.kitpvp.commands.KitPvPCmd;
import eu.iteije.kitpvp.commands.LeaderboardCmd;

public class RegisterCommands {

    public RegisterCommands(KitPvP instance) {
        registerCommands(instance);
    }

    // Register all listeners
    private void registerCommands(KitPvP instance) {
        // KitPvP command
        instance.getCommand("kitpvp").setExecutor(new KitPvPCmd(instance));
        // Leaderboard command
        instance.getCommand("leaderboard").setExecutor(new LeaderboardCmd(instance));
    }

}
