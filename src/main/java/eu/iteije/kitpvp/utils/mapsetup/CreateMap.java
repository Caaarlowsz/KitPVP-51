package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.objects.Help;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CreateMap {

    // Main class instance
    private final KitPvP instance;

    // Here is the inventory of the player saved while in setup
    // If this HashMap contains the UUID of a player, the player is in setup
    public static HashMap<UUID, ItemStack[]> savedInventories = new HashMap<>();

    // Map names
    public static HashMap<UUID, String> mapNames = new HashMap<>();

    // In this case, only a explanation is given, so the second thing isn't used whatsoever
    private final List<String> explanation = Arrays.asList(
            "&fPlace and remove &6gold pressure plates &ffor creating spawnpoints.",
            "&fIf you're done and want to save the map, click &aFinish&f. If you want to remove everything and stop the setup process, click &cCancel&f."
    );
    private final List<String> empty = Arrays.asList(
            "",
            "",
            ""
    );

    // Help class / CreateMap instances
    private final Help help = new Help(explanation, empty);

    public CreateMap(KitPvP instance) {
        this.instance = instance;
    }

    // Start setup call
    public void startSetup(Player player, String[] args) {
        // Send some explanation of the setup tool here
        help.send(player);
        // Save given map name
        mapNames.put(player.getUniqueId(), args[1]);
        // Give tools to the player
        giveTools(player);
    }

    private static Tool exitTool;
    private static Tool spawnpointTool;
    private static Tool finishTool;

    // Give setup tools to player
    private void giveTools(Player player) {
        // Instance of ConfigFile
        PluginFile configFile = KitPvP.getInstance().getConfigFile();

        // Create new itemstacks (setup tools, including exit/finish block);

        // Exit block
        exitTool = new Tool(new ItemStack(Material.RED_WOOL, 1).getType(), 0, configFile.get().getString("mapsetup.tools.exit.name")); // red wool
        // Spawnpoint block
        spawnpointTool = new Tool(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 4, configFile.get().getString("mapsetup.tools.spawnpoint.name")); // gold pressure plate
        // Finish block
        finishTool = new Tool(new ItemStack(Material.LIME_WOOL, 1).getType(), 8, configFile.get().getString("mapsetup.tools.finish.name")); // lime wool

        // Save player inventory
        savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();

        // Call method from Tool to set tool to player inventory
        exitTool.setToInventory(player);
        spawnpointTool.setToInventory(player);
        finishTool.setToInventory(player);

        // Set gamemode to creative
        player.setGameMode(GameMode.CREATIVE);
    }

    public static void stopSetup(Player player, boolean forced) {
        // Send 'forced' message to player if forced is true
        if (forced) {
            Message.sendToPlayer(player, Message.get("createmap_force_stop"), true);
        }
        // Delete saved map name
        mapNames.remove(player.getUniqueId());
        // Return inventory method call
        returnInventory(player);
        // Delete all spawnpoints
        // Instance of SpawnPlate from ToolActions
        SpawnPlate spawnPlate = ToolActions.getSpawnPlate();
        try {
            // Get list of locations
            List<Location> locations = ToolActions.getSpawnPlate().getLocations().get(player.getUniqueId());
            // Loop through locations
            for (Location location : locations) {
                spawnPlate.destroyPlate(player, location, false);
            }
            // Remove saved data
            spawnPlate.removeLocations(player);
        } catch (NullPointerException | ConcurrentModificationException exception) {
            // Leave it empty, it's just a handler
        }
    }

    // Get map name (players second argument)
    public static String getMapName(Player player) {
        return mapNames.get(player.getUniqueId());
    }

    // Return inventory to a specific player in setup
    public static void returnInventory(Player player) {
        // Clear inventory
        player.getInventory().clear();
        // Return old inventory contents
        player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
        // Get the player out of savedInventories (which also serves as 'inSetup' variable)
        savedInventories.remove(player.getUniqueId());
    }


    /**
     * @param material material to check
     * @param player   player to get held item slot from
     * @return true if material is setup block
     * Check if the material is a setup item and returns the tool
     */
    public static String checkSetupItem(Material material, Player player) {
        // Interacted inventory slot
        int heldItemSlot = player.getInventory().getHeldItemSlot();

        if (savedInventories.containsKey(player.getUniqueId())) {
            if (material == exitTool.getMaterial() && heldItemSlot == exitTool.getInventorySlot()) return "exit";
            if (material == spawnpointTool.getMaterial() && heldItemSlot == spawnpointTool.getInventorySlot())
                return "spawnpoint";
            if (material == finishTool.getMaterial() && heldItemSlot == finishTool.getInventorySlot()) return "finish";
        }

        return "none";
    }
}
