package eu.iteije.kitpvp;

import eu.iteije.kitpvp.commands.KitPvPCmd;

public class RegisterCommands {

    // Instance variable of main class
    private KitPvP instance;

    public RegisterCommands(KitPvP instance) {
        this.instance = instance;
        registerCommands();
    }

    // Register all listeners
    private void registerCommands() {
        // KitPvP command
        instance.getCommand("kitpvp").setExecutor(new KitPvPCmd(instance));
    }

}
