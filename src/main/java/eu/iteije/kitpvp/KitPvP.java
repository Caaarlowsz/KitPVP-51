package eu.iteije.kitpvp;

import eu.iteije.kitpvp.data.MySQL;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.files.MessageFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitPvP extends JavaPlugin {

    // Create private instance variable
    private static KitPvP instance;

    @Override
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
        MySQL.getDatabase().checkTable();

        // Register event listeners
        new RegisterListeners(this);

        // Register commands
        new RegisterCommands(this);


        // Send enabled message to console
        Message.sendToConsole("&fPlugin enabled!", true);
    }

    @Override
    public void onDisable() {
        // Loop through all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                // If player is in setup, force stop and return inventory
                if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
                    CreateMap.stopSetup(player, true);
                }
                // If player is in game, force stop and return inventory
                if (Game.playersInGame.containsKey(player.getUniqueId())) {
                    Game.leave(player);
                }
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
