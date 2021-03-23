package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.memory.GameLocations;
import eu.iteije.kitpvp.memory.SpawnLocation;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectSpawn {

    private static String SELECT_SPAWN_INVENTORY_TITLE = TransferMessage.replaceColorCodes("&8Select Spawn");

    public static Map<UUID, String> hasInventoryOpen = new HashMap<>();

    // All allowed inventory slots (kits will be placed here)
    public static Integer[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    private final Player player;
    private final String map;
    private final GameLocations locations;

    public SelectSpawn(String map, GameLocations locations, Player player) {
        this.locations = locations;
        this.map = map;
        this.player = player;
    }

    public void openMenu() {
        Inventory inventory = Bukkit.createInventory(null, 54, SELECT_SPAWN_INVENTORY_TITLE);

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cClose"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        ItemStack centerItem = InventoryItem.createItem(Material.PAPER, 1, "&dCenter");
        inventory.setItem(allowedSlots[0], centerItem);

        int count = 1;
        for (Map.Entry<String, SpawnLocation> entry : locations.getSpawnPoints().entrySet()) {
            if (count < (allowedSlots.length)) { // remove 1 slot due to the center spawn being loaded separately
                ItemStack spawnItem = InventoryItem.createItem(entry.getValue().getMaterial(), 1, "&d" + entry.getKey());
                inventory.setItem(allowedSlots[count], spawnItem);
                count++;
            }
        }

        player.openInventory(inventory);
        hasInventoryOpen.put(player.getUniqueId(), this.map);
    }



}
