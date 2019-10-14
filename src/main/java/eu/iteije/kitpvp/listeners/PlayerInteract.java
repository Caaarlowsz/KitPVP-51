package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.OpenInventory;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import eu.iteije.kitpvp.utils.mapsetup.ToolActions;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

    // Instance of Game
    public static Game game;

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

        // Cancel interact event if player is in game
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            // If a players jumps on wheat or other vulnerable stuff, event is cancelled
            if (event.getAction() == Action.PHYSICAL) {
                event.setCancelled(true);
            }
        }

        // If player is in isEditingIcon HashMap, proceed
        if (EditKits.isEditingIcon.containsKey(player.getUniqueId())) {
            // Server has to give a response for every action, except for the PHYSICAL one
            if (action != Action.PHYSICAL) {
                // Proceed if item in hand is not null
                if (event.getItem() != null) {
                    // Call editKitIcon method in EditKits
                    EditKits editKits = new EditKits(player);
                    editKits.editKitIcon(event.getItem());

                    // Remove player from isEditingIcon HashMap
                    EditKits.isEditingIcon.remove(player.getUniqueId());

                    // Open EditKitInventory
                    OpenInventory openInventory = new OpenInventory("editkit", player);
                    openInventory.openInventory();
                }
            }
            // Cancel event
            event.setCancelled(true);
        }

        // Left/right click block check | SIGNS
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
                        if (mapFile.get().getConfigurationSection("maps." + mapName) != null) {
                            // Check whether the player is ingame or not
                            if (!Game.playersInGame.containsKey(player.getUniqueId())) {
                                // Check if player is editing a kit
                                if (!EditKits.selectedKit.containsKey(player.getUniqueId())) {
                                    SpawnSubCmd spawnSubCmd = new SpawnSubCmd(instance);
                                    if (spawnSubCmd.getSpawnSet()) {
                                        SelectKit selectKit = new SelectKit(player, mapName);
                                        selectKit.openMenu();
                                    } else {
                                        // Send no lobbyspawn message
                                        Message.sendToPlayer(player, Message.get("interactsign_no_spawn"), true);
                                    }
                                } else {
                                    // No access
                                    Message.sendToPlayer(player, Message.get("editkits_access_error"), true);
                                }
                            } else {
                                // Already in game error
                                Message.sendToPlayer(player, Message.get("interactsign_ingame_error"), true);
                            }

                        } else {
                            // Map doesn't exists error
                            Message.sendToPlayer(player, Message.get("interactsign_map_error"), true);
                        }
                    }
                }
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
                    String setupItem = CreateMap.checkSetupItem(player.getInventory().getItemInMainHand().getType(), player);
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
                String setupItem = CreateMap.checkSetupItem(player.getInventory().getItemInMainHand().getType(), player);
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
