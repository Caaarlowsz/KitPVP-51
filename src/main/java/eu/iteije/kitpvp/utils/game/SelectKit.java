package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SelectKit {

    // Title of select menu
    public static String SELECT_KIT_INVENTORY_TITLE = TransferMessage.replaceColorCodes("&c&lSelecteer een kit");

    // Assigned player
    private Player player;
    // Map the player is joining
    private String map;

    // All allowed inventory slots (kits will be placed here)
    public static Integer[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    // If select kit inventory is open, player is added to this map
    public static HashMap<UUID, String> hasInventoryOpen = new HashMap<>();

    /**
     * @param player player who clicked on a KitPvP sign
     * @param map the map showed on the clicked sign
     */
    public SelectKit(Player player, String map) {
        this.player = player;
        this.map = map;
    }

    // Open new inventory
    public void openMenu() {
        // Create new inventory
        Inventory inventory = Bukkit.createInventory(null, 54, SELECT_KIT_INVENTORY_TITLE);

        // Add kits to inventory
        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Put kits into inventory
        int count = 0;
        for (String kitName : kitFile.get().getConfigurationSection("kits").getKeys(false)) {
            if (count < allowedSlots.length) {
                Material material = Material.getMaterial(kitFile.get().getString("kits." + kitName + ".icon").toUpperCase());
                ItemStack kitItemStack = new ItemStack(material, 1);
                ItemMeta itemMeta = kitItemStack.getItemMeta();

                itemMeta.setDisplayName(TransferMessage.replaceColorCodes("&c" + kitName));
                kitItemStack.setItemMeta(itemMeta);
                inventory.setItem(allowedSlots[count], kitItemStack);
                count++;
            }
        }

        // Open inventory with all contents
        player.openInventory(inventory);
        hasInventoryOpen.put(player.getUniqueId(), map);
    }

    // Give kit
    public void giveKit(Player player, String kitName) {
        try {
            // Instance of KitFile
            KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

            // Predefine a list where the item stacks are saved in
            List<ItemStack> kit = new ArrayList<>();

            for (String item : kitFile.get().getConfigurationSection("kits." + kitName + ".items").getKeys(false)) {
                kit.add(new ItemStack(Material.getMaterial(item), kitFile.get().getInt("kits." + kitName + ".items." + item + ".amount")));
            }

            // Save player inventory in a HashMap (located in Game class)
            Game.savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
            // Clear player inventory
            player.getInventory().clear();

            // Give kit
            ItemStack[] kitItems = kit.toArray(new ItemStack[0]);
            player.getInventory().setContents(kitItems);

            // Set gear
            player.getInventory().setHelmet(getGearPiece(kitName, "HELMET", kitFile));
            player.getInventory().setChestplate(getGearPiece(kitName, "CHESTPLATE", kitFile));
            player.getInventory().setLeggings(getGearPiece(kitName, "LEGGINGS", kitFile));
            player.getInventory().setBoots(getGearPiece(kitName, "BOOTS", kitFile));

            // Join game
            Game game = new Game(player, map, KitPvP.getInstance());
            game.join();
        } catch (NullPointerException exception) {
            // Send error message to player
            Message.sendToPlayer(player, Message.get("joingame_kit_error"), true);

            exception.printStackTrace();
        }
    }

    private ItemStack getGearPiece(String kit, String piece, KitFile kitFile) {
        try {
            Material material = Material.getMaterial(kitFile.get().getString("kits." + kit + ".gear." + piece.toUpperCase()));
            ItemStack itemStack = new ItemStack(material, 1);
            Message.broadcast(itemStack.toString(), true);
            return itemStack;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ItemStack(Material.AIR);
    }
}
