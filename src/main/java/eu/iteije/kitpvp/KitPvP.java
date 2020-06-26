package eu.iteije.kitpvp;

import eu.iteije.kitpvp.data.DataHandler;
import eu.iteije.kitpvp.data.MySQL;
import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.files.MessageFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.runnables.LeaderboardUpdater;
import eu.iteije.kitpvp.utils.Scoreboard;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class KitPvP extends JavaPlugin {

    // Create private instance variable
    private static KitPvP instance;

    public List<Integer> tasks = new ArrayList<>();

    @Override @Deprecated
    public void onEnable() {
        // Define instance
        instance = this;

        // Load files
        new ConfigFile(this, true);
        new MessageFile(this, true);
        new MapFile(this, true);
        new KitFile(this, true);

        // Open database connection
        MySQL.getDatabase().openConnection();

        // Register event listeners
        new RegisterListeners(this);

        // Register commands
        new RegisterCommands(this);

        tasks.add(new LeaderboardUpdater(this).start());

        // Reload player data and scoreboard (rip tps)
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            // Loop through all players
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Remove existing player data
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
        // Loop through all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                UUID uuid = player.getUniqueId();
                // If player is in setup, force stop and return inventory
                if (CreateMap.savedInventories.containsKey(uuid)) CreateMap.stopSetup(player, true);

                // If player has the select kit/edit kit menu open -> close it
                if (SelectKit.hasInventoryOpen.containsKey(uuid) || EditKits.currentInventory.containsKey(uuid)) player.closeInventory();

                // If player is in game, force stop and return inventory
                if (Game.playersInGame.containsKey(uuid)) Game.leave(player);

            } catch (NoClassDefFoundError exception) {
                // Empty catch block, you're right, this is just to prevent console spam in case something doesn't work, it does what it is intended to do
                // Put a printStackTrace() here if suspicious activity is detected
            }
        }

        // Send disabled message to console
        Message.sendToConsole("&c[KitPvP] &fPlugin disabled!", false);
    }

    // Global KitPvP (main) class instance method
    public static KitPvP getInstance() {
        return instance;
    }
}
