package eu.iteije.kitpvp;

import eu.iteije.kitpvp.listeners.Interact;
import eu.iteije.kitpvp.listeners.SignChange;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {

    // Instance variable of main class
    private KitPvP instance;

    public RegisterListeners(KitPvP instance) {
        this.instance = instance;
        registerListeners();
    }

    // Register all listeners
    private void registerListeners() {
        PluginManager pluginManager = instance.getServer().getPluginManager();

        // PlayerInteractEvent
        pluginManager.registerEvents(new Interact(instance), instance);
        // SignChangeEvent
        pluginManager.registerEvents(new SignChange(instance), instance);
    }

}
