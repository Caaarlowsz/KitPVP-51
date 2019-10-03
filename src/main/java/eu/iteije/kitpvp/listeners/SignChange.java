package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {

    // Instance variable of main class
    private KitPvP instance;
    private ConfigFile configFile;

    public SignChange(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        configFile = new ConfigFile(instance, false);

        // Define player executing this event
        Player player = event.getPlayer();
        // Define registered sign prefix/header
        String signHeader = configFile.get().getString("sign_prefix");

        if (event.getLine(0).equalsIgnoreCase("[kitpvp]")) {
            if (player.hasPermission("kitpvp.admin.sign")) {
                if (!event.getLine(1).isEmpty()) {
                    // Convert the sign header to a colored sign header
                    signHeader = TransferMessage.replaceColorCodes(configFile.get().getString("sign_prefix"));
                    // Set the first line to the colorized sign header
                    event.setLine(0, signHeader);

                    // Set the second line (line 1 or map) uppercase
                    event.setLine(1, event.getLine(1).toUpperCase());
                } else {
                    Message.sendToPlayer(player, Message.get("placesign_empty_map_line"), true);
                }
            } else {
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }
        }
    }

}
