package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import eu.iteije.kitpvp.utils.mapsetup.ToolActions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    // Log placed blocks if player is in setup

    // Instance variable of main class
    private KitPvP instance;
    private ConfigFile configFile;

    public BlockPlace(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        configFile = new ConfigFile(instance, false);

        // Define player executing this event
        Player player = event.getPlayer();
        // Block placed
        Block block = event.getBlockPlaced();

        // Check whether the player is in setup or not
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            // Double check used block is a setup block, if so, proceed
            String setupItem = CreateMap.checkSetupItem(block.getType(), player);
            if (!setupItem.equals("none")) {
                event.setCancelled(true);
                switch (setupItem) {
                    case "exit":
                        ToolActions.useExitTool(player);
                        break;
                    case "spawnpoint":
                        ToolActions.useSpawnpointTool(player);
                        break;
                    case "finish":
                        ToolActions.useFinishTool(player);
                        break;
                    default:
                        break;
                }
            }
            Message.sendToPlayer(player, setupItem, true);
        }
    }
}
