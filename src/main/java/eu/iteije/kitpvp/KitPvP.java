package eu.iteije.kitpvp;

import eu.iteije.kitpvp.commands.KitPvPCmd;
import eu.iteije.kitpvp.commands.LeaderboardCmd;
import eu.iteije.kitpvp.data.DataHandler;
import eu.iteije.kitpvp.data.MySQL;
import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.runnables.LeaderboardUpdater;
import eu.iteije.kitpvp.runnables.PingUpdater;
import eu.iteije.kitpvp.utils.Scoreboard;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class KitPvP extends JavaPlugin {

    // Create private instance variable
    private static KitPvP instance;

    private MySQL sqlModule;

    public List<Integer> tasks = new ArrayList<>();

    // Files
    @Getter private PluginFile kitFile;
    @Getter private PluginFile messageFile;
    @Getter private PluginFile mapFile;
    @Getter private PluginFile configFile;

    @Override
    @Deprecated
    public void onEnable() {
        // Define instance
        instance = this;

        // Load files
        this.configFile = new PluginFile(this, "config.yml");
        this.messageFile = new PluginFile(this, "messages.yml");
        this.mapFile = new PluginFile(this, "maps.yml");
        this.kitFile = new PluginFile(this, "kits.yml");

        // Open database connection
        this.sqlModule = MySQL.getDatabase(); // never do this folks
        this.sqlModule.openConnection();

        this.registerCommands();

        // Register event listeners
        new RegisterListeners(this);

        tasks.add(new LeaderboardUpdater(this).start());
        tasks.add(new PingUpdater(this).start());

        // Reload player data and scoreboard (rip tps)
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            // Loop through all players
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Remove existing player data.
                UserCache.removeUUID(player.getUniqueId());
                // Load player
                DataHandler.getHandler().loadPlayer(player.getUniqueId());

                // Wait a few seconds, to prevent NullPointerExceptions (it can take a few seconds to load player data)
                getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> Scoreboard.load(player), 100L); // delay / 20 = seconds
            }
        }, 60L); // delay / 20 = seconds


        // Send enabled message to console
        Message.sendToConsole("&fPlugin enabled!", true);
    }

    @Override
    public void onDisable() {
        this.sqlModule.closeConnection(true);
        // Loop through all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                UUID uuid = player.getUniqueId();
                // If player is in setup, force stop and return inventory
                if (CreateMap.savedInventories.containsKey(uuid)) CreateMap.stopSetup(player, true);

                // If player has the select kit kit menu open -> close it
                if (SelectKit.hasInventoryOpen.containsKey(uuid)) player.closeInventory();

                // If player is in game, force stop and return inventory
                if (Game.playersInGame.containsKey(uuid)) Game.leave(player);

            } catch (NoClassDefFoundError ignored) {
            }
        }

        // Send disabled message to console
        Message.sendToConsole("&c[KitPvP] &fPlugin disabled!", false);
    }

    // Global KitPvP (main) class instance method
    public static KitPvP getInstance() {
        return instance;
    }


    // Register all listeners
    private void registerCommands() {
        // KitPvP command
        getCommand("kitpvp").setExecutor(new KitPvPCmd(this));
        // Leaderboard command
        getCommand("leaderboard").setExecutor(new LeaderboardCmd(this));
    }
}
