package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    // Instance variable of main class
    private KitPvP instance;
    private ConfigFile configFile;
    private MapFile mapFile;

    public PlayerInteract(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        configFile = new ConfigFile(instance, false);
        mapFile = new MapFile(instance, false);

        // Define player executing this event
        Player player = event.getPlayer();
        // Action performed by player
        Action action = event.getAction();
        // Clicked material > AIR by default
        Material clickedBlock = Material.AIR;
        try {
             clickedBlock = event.getClickedBlock().getType();
        } catch (NullPointerException exception) {
            // Handle NullPointerException > If player is hitting the air, NullPointerException (.getType()) will be triggered
        }

        // Left/right click block check
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            // Checking whether the clicked block is a sign
            if (clickedBlock == Material.SIGN || clickedBlock == Material.WALL_SIGN) {
                // Checking item in playerhand; it have to be destructible
                // A KITPVP sign is ONLY destructible by an AXE
                if (!player.getItemInHand().toString().contains("AXE")) {
                    // Get clicked sign and get expected header from config
                    Sign sign = (Sign) event.getClickedBlock().getState();
                    String signHeader = TransferMessage.replaceColorCodes(configFile.get().getString("sign_prefix")).toLowerCase();

                    // Compare sign prefix from config with the header of the interacted sign
                    if (sign.getLine(0).toLowerCase().equals(signHeader)) {
                        // Get map name at sign line 1 (for a player line 2)
                        String mapName = sign.getLine(1).toUpperCase();
                        // Check whether the map name/map data is known
                        if (mapFile.get().getConfigurationSection(mapName) != null) {
                            // Continue here
                        } else {
                            // Map doesn't exists error
                            Message.sendToPlayer(player, Message.get("interactsign_map_error"), true);
                        }
                    }
                }
            }
        }
    }
}
