package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import eu.iteije.kitpvp.utils.mapsetup.ToolActions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    // Instances of files
    private PluginFile configFile;
    private PluginFile mapFile;

    // Instance of Game
    public static Game game;

    public PlayerInteract(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        configFile = KitPvP.getInstance().getConfigFile();
        this.mapFile = KitPvP.getInstance().getMapFile();

        // Define player executing this event
        Player player = event.getPlayer();
        // Action performed by player
        Action action = event.getAction();

        // Cancel interact event if player is in game
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            // If a players jumps on wheat or other vulnerable stuff, event is cancelled
            if (event.getAction() == Action.PHYSICAL) {
                event.setCancelled(true);
            }
        }

        // Right click block only
        if (action == Action.RIGHT_CLICK_BLOCK) {
            if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
                // Checking whether the clicked block is a setup block and which it is
                String setupItem = CreateMap.checkSetupItem(player.getInventory().getItemInMainHand().getType(), player);
                if (!setupItem.equals("none")) {
                    // Use setup item
                    event.setCancelled(true);
                    useTool(setupItem, player, true, event.getClickedBlock());
                }
            }

        }

        // Left click block only
        if (action == Action.LEFT_CLICK_BLOCK) {
            if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
                if (event.getClickedBlock().getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                    // Redirect to BlockBreak if block is not solid
                    if (!event.getClickedBlock().getType().isSolid()) {
                        event.setCancelled(false);
                        return;
                    }

                    // Checking whether the clicked block is a setup block and which it is
                    String setupItem = CreateMap.checkSetupItem(player.getInventory().getItemInHand().getType(), player);
                    if (!setupItem.equals("none")) {
                        // Use setup item
                        event.setCancelled(true);
                        useTool(setupItem, player, false, event.getClickedBlock());
                    }
                }
            }
        }

        // Left/right click air
        if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
            if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
                // Checking whether the clicked block is a setup block and which it is
                String setupItem = CreateMap.checkSetupItem(player.getInventory().getItemInHand().getType(), player);
                if (!setupItem.equals("none")) {
                    // Use setup item
                    event.setCancelled(true);
                    useTool(setupItem, player, false, null);
                }
            }
        }
    }


    // Use tool
    private void useTool(String setupItem, Player player, boolean includeSpawnpoint, Block interactedBlock) {
        if (includeSpawnpoint) {
            switch (setupItem) {
                case "exit":
                    ToolActions.useExitTool(player);
                    break;
                case "spawnpoint":
                    ToolActions.useSpawnpointTool(player, interactedBlock);
                    break;
                case "finish":
                    ToolActions.useFinishTool(player);
                    break;
                default:
                    break;
            }
        } else {
            switch (setupItem) {
                case "exit":
                    ToolActions.useExitTool(player);
                    break;
                case "finish":
                    ToolActions.useFinishTool(player);
                    break;
                default:
                    break;
            }
        }

    }
}
