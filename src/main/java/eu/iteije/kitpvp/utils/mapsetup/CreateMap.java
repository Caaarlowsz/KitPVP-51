package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.Help;
import eu.iteije.kitpvp.files.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateMap {

    // Main class instance
    private KitPvP instance;

    // Here is the inventory of the player saved while in setup
    // If this HashMap contains the UUID of a player, the player is in setup
    public static HashMap<UUID, ItemStack[]> savedInventories = new HashMap<>();

    // In this case, only a explanation is given, so the second thing isn't used whatsoever
    private List<String> explanation = Arrays.asList(
            "Om spawnpoints te maken/verwijderen, moet je gold pressure plates plaatsen/weghalen.",
            "Als je klaar bent, druk je op het &agroene &fwol blok. Als je wil stoppen en alles wil verwijderen, druk je op het &crode &fwol blok.",
            "Alle spawns zijn later nog te veranderen met /kitpvp editmap <mapnaam>"
    );
    private List<String> empty = Arrays.asList(
            "",
            "",
            ""
    );

    // Help class / CreateMap instances
    private Help help = new Help(explanation, empty);

    public CreateMap(KitPvP instance) {
        this.instance = instance;
    }

    // Start setup call
    public void startSetup(Player player, String[] args) {
        // Send some explanation of the setup tool here
        help.send(player);
        // Give tools to the player
        giveTools(player);
    }

    private static Tool exitTool;
    private static Tool spawnpointTool;
    private static Tool finishTool;

    // Give setup tools to player
    private void giveTools(Player player) {
        // Instance of ConfigFile
        ConfigFile configFile = new ConfigFile(instance, false);

        // Create new itemstacks (setup tools, including exit/finish block);

        // Exit block
        exitTool = new Tool(Material.RED_WOOL, 0, configFile.get().getString("mapsetup.tools.exit.name")); // red wool
        // Spawnpoint block
        spawnpointTool = new Tool(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 4, configFile.get().getString("mapsetup.tools.spawnpoint.name")); // gold pressure plate
        // Finish block
        finishTool = new Tool(Material.LIME_WOOL, 8, configFile.get().getString("mapsetup.tools.finish.name")); // lime wool

        // Save player inventory
        savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();

        // Call method from Tool to set tool to player inventory
        exitTool.setToInventory(player);
        spawnpointTool.setToInventory(player);
        finishTool.setToInventory(player);

        // Wait a few seconds then give inventory back
        // If the player logs out before the returninventory is called, nullpointerexception is given
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> {
            returnInventory(player);
        }, 20 * 5);

    }

    // TODO: force stop a setup
    public static void forceStop(Player player) {

    }

    // Return inventory to a specific player in setup
    public static void returnInventory(Player player) {
        // Clear inventory
        player.getInventory().clear();
        // Return old inventory contents
        player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
        // Get the player out of savedInventories (which also serves as inSetup variable)
        savedInventories.remove(player.getUniqueId());
    }


    /**
     * @param material material to check
     * @param player player to get held item slot from
     * @return true if material is setup block
     * Check if the material is a setup item and returns the tool
     */
    public static String checkSetupItem(Material material, Player player) {
        // Interacted inventory slot
        int heldItemSlot = player.getInventory().getHeldItemSlot();

        if (savedInventories.containsKey(player.getUniqueId())) {
            if (material == exitTool.getMaterial() && heldItemSlot == exitTool.getInventorySlot()) return "exit";
            if (material == spawnpointTool.getMaterial() && heldItemSlot == spawnpointTool.getInventorySlot()) return "spawnpoint";
            if (material == finishTool.getMaterial() && heldItemSlot == spawnpointTool.getInventorySlot()) return "finish";
        }

        return "none";
    }
}
