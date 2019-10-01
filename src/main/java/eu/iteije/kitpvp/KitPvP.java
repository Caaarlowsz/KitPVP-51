package eu.iteije.kitpvp;

import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.files.MessageFile;
import eu.iteije.kitpvp.listeners.RegisterListeners;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitPvP extends JavaPlugin {

    // Create private instance variable
    private static KitPvP instance;


    @Override
    public void onEnable() {
        // Define instance
        instance = this;

        // Load files;
        // Load config.yml
        new ConfigFile(this, true);
        // Load messages.yml
        new MessageFile(this, true);
        // Load maps.yml
        new MapFile(this, true);
        // Load kits.yml
        new KitFile(this, true);

        // Register event listeners
        new RegisterListeners(this);

        // Register commands
        registerCommands();

        // Send enabled message to console
        Message.sendToConsole("&fPlugin enabled!", true);
    }

    @Override
    public void onDisable() {
        // Send disabled message to console
        Message.sendToConsole("&c[KitPvP] &fPlugin disabled!", false);
    }

    // Register command classes
    private void registerCommands() {

    }


    // Global KitPvP (main) class instance method
    public static KitPvP getInstance() {
        return instance;
    }
}
