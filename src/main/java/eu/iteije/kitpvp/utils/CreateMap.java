package eu.iteije.kitpvp.utils;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.Help;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateMap {

    // Main class instance
    private KitPvP instance;

    // Here is the inventory of the player saved while in setup
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
        giveTools(player);
    }

    // Give setup tools to player
    private void giveTools(Player player) {
        // Create new itemstacks (setup tools, including exit/finish block);

        // Exit block
        ItemStack exitTool = new ItemStack(Material.RED_WOOL, 1); // red wool
        // Spawnpoint block
        ItemStack spawnTool = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1); // gold pressure plate
        // Finish block
        ItemStack finishTool = new ItemStack(Material.LIME_WOOL, 1); // lime wool


        // Save player inventory
        savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();

        // [TEMP] Call setTool method to set item to inventory
        setTool(player, exitTool, 0, "&cStoppen");
        setTool(player, spawnTool, 4, "&eSpawnpoint");
        setTool(player, finishTool, 8, "&aKlaar");

        // Wait 3 seconds then give inventory back
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                player.getInventory().clear();
                player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
                savedInventories.remove(player.getUniqueId());
            }
        }, 20 * 5);

    }

    // Set item to inventory
    private void setTool(Player player, ItemStack tool, int slot, String toolName) {
        // Set item name
        ItemMeta itemMeta = tool.getItemMeta();
        itemMeta.setDisplayName(TransferMessage.replaceColorCodes(toolName));
        tool.setItemMeta(itemMeta);

        // Set item to inventory at given slot
        player.getInventory().setItem(slot, tool);
    }
}
