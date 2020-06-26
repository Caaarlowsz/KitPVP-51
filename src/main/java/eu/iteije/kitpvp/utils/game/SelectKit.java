package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SelectKit {

    // Title of select menu
    public static String SELECT_KIT_INVENTORY_TITLE = TransferMessage.replaceColorCodes("&c&lSelect a kit");

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

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cSluit menu"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        // Put kits into inventory
        int count = 0;
        // Loop through all kits
        for (String kitName : kitFile.get().getConfigurationSection("kits").getKeys(false)) {
            // Check whether the amount kits in the inventory is not higher than the amount of allowed slots
            if (count < allowedSlots.length) {
                // Get icon material from config
                Material material = Material.getMaterial(kitFile.get().getString("kits." + kitName + ".icon").toUpperCase());
                // Create new itemstack
                ItemStack kitItemStack = new ItemStack(material, 1);
                // Get item meta from kitItemStack
                ItemMeta kitItemMeta = kitItemStack.getItemMeta();

                // Try hiding the attributes
                try {
                    kitItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                } catch (NullPointerException exception) {
                    // Leave it empty
                }


                // Change display name
                kitItemMeta.setDisplayName(TransferMessage.replaceColorCodes("&c" + kitName));
                // Assign new itemmeta to the itemstack
                kitItemStack.setItemMeta(kitItemMeta);
                // Assign itemstack to the next allowed slot
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

            // Loop through each item in the 'items' section of a kit
            for (String item : kitFile.get().getConfigurationSection("kits." + kitName + ".items").getKeys(false)) {
                ItemStack kitItem = new ItemStack(Material.getMaterial(kitFile.get().getString("kits." + kitName + ".items." + item + ".item")),
                        kitFile.get().getInt("kits." + kitName + ".items." + item + ".amount"));
                ItemMeta meta = kitItem.getItemMeta();
                List<String> enchantments = kitFile.get().getStringList("kits." + kitName + ".items." + item + ".enchantments");

                for (String enchantmentItem : enchantments) {
                    try {
                        String[] data = enchantmentItem.split(":");
                        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(data[0].toLowerCase()));
                        meta.addEnchant(enchantment, Integer.parseInt(data[1]), true);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                }
                kitItem.setItemMeta(meta);

                kit.add(kitItem);
            }

            for (String effect : kitFile.get().getStringList("kits." + kitName + ".effects")) {
                String[] data = effect.split(":");
                PotionEffectType effectType = PotionEffectType.getByName(data[0]);
                if (effectType == null) continue;

                int amp = data[1] != null ? Integer.parseInt(data[1]) - 1 : 1;

                player.addPotionEffect(effectType.createEffect(3600 * 20, amp));
            }

            // Save player inventory in a HashMap (located in Game class)
            Game.savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
            // Clear player inventory
            player.getInventory().clear();

            // Give kit
            ItemStack[] kitItems = kit.toArray(new ItemStack[0]);
            player.getInventory().setContents(kitItems);



            // Set gear
            // AFTER SEEING THIS CODE, I'M SURE YOU THINK I JUST STARTED LEARNING JAVA OR SOMETHING
            try {
                player.getInventory().setHelmet(getGearPiece(kitName, "HELMET", kitFile, true));
            } catch (Exception exception) {

            }

            try {
                player.getInventory().setChestplate(getGearPiece(kitName, "CHESTPLATE", kitFile, true));
            } catch (Exception exception) {

            }

            try {
                player.getInventory().setLeggings(getGearPiece(kitName, "LEGGINGS", kitFile, true));
            } catch (Exception exception) {

            }

            try {
                player.getInventory().setBoots(getGearPiece(kitName, "BOOTS", kitFile, true));
            } catch (Exception exception) {

            }
            player.setAllowFlight(false);
            player.setFlying(false);

            // Join game
            Game game = new Game(player, map, KitPvP.getInstance());
            game.join();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            // Send error message to player
            Message.sendToPlayer(player, Message.get("joingame_kit_error"), true);
        }
    }

    private ItemStack getGearPiece(String kit, String piece, KitFile kitFile, boolean unbreakable) {
        try {
            // Get material from kit file and create new itemstack
            Material material = Material.getMaterial(kitFile.get().getString("kits." + kit + ".gear." + piece.toUpperCase() + ".item"));
            ItemStack itemStack = new ItemStack(material, 1);
            // If boolean unbreakable is true, assign unbreakable to the itemstack
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (unbreakable) {
<<<<<<< HEAD
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemStack.setItemMeta(itemMeta);
=======
                itemMeta.setUnbreakable(true);
>>>>>>> modern
            }

            List<String> enchantments = kitFile.get().getStringList("kits." + kit + ".gear." + piece.toUpperCase() + ".enchantments");

            for (String enchantmentItem : enchantments) {
                String[] data = enchantmentItem.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(data[0].toLowerCase()));
                itemMeta.addEnchant(enchantment, Integer.parseInt(data[1]), true);
            }

            // Set item meta
            itemStack.setItemMeta(itemMeta);
            // Return itemstack of gear piece
            return itemStack;
        } catch (IllegalArgumentException | NullPointerException exception) {
            // No stack trace, in case a gear piece is not assigned, you would get a ton of unnecessary errors
            // Return empty itemstack
            return new ItemStack(Material.AIR);
        }
    }
}
