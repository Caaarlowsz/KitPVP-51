package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;

public class MapsSubCmd {

    // Instance of main class
    private KitPvP instance;

    // No help page for this command

    // Maps.yml
    private PluginFile mapFile;

    /**
     * @param instance instance of KitPvP (main) class
     */
    public MapsSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Define instance of MapFile
        this.mapFile = KitPvP.getInstance().getMapFile();

        if (!mapFile.get().getConfigurationSection("maps").getKeys(false).isEmpty()) {
            // Title message
            Message.sendToSender(sender, Message.get("maps_title"), true);
            // Content
            // Loop through all map names
            for (String key : mapFile.get().getConfigurationSection("maps").getKeys(false)) {
                String message = Message.get("maps_content");
                message = Message.replace(message, "{map}", key.toUpperCase());
                Message.sendToSender(sender, message, true);
            }
        } else {
            // Map list is empty
            Message.sendToSender(sender, Message.get("maps_empty"), true);
        }
    }
}
