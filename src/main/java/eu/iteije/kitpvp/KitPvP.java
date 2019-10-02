package eu.iteije.kitpvp;

import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.files.MessageFile;
import eu.iteije.kitpvp.pluginutils.Message;
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

        // Register event listeners
        new RegisterListeners(this);

        // Register commands
        new RegisterCommands(this);

        // Send enabled message to console
        Message.sendToConsole("&fPlugin enabled!", true);
    }

    @Override
    public void onDisable() {
        // Send disabled message to console
        Message.sendToConsole("&c[KitPvP] &fPlugin disabled!", false);
    }

    // Global KitPvP (main) class instance method
    public static KitPvP getInstance() {
        return instance;
    }
}
