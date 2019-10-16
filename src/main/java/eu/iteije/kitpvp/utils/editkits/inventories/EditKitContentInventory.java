package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.NewKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EditKitContentInventory {

    // Inventory title
    public static String EDIT_KIT_CONTENT_TITLE = TransferMessage.replaceColorCodes("&c&lBewerk de items");

    // Old inventory items are saved here (to compare to the new ones)
    public static HashMap <UUID, ItemStack[]> currentItems = new HashMap<>();

    // Undefined player variable
    private Player player;

    /**
     * @param player player the inventory will be opened for
     */
    public EditKitContentInventory(Player player) {
        this.player = player;
    }

    public void open() {
        // New instance of Inventory, setting the size to 54 slots and the title
        Inventory inventory = Bukkit.createInventory(null, 54, EDIT_KIT_CONTENT_TITLE);

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cSluit menu"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        // Return to previous menu item
        ItemStack returnItem = InventoryItem.createItem(Material.SPECTRAL_ARROW, 1, "&cGa terug"); // create new itemstack
        inventory.setItem(45, returnItem); // assign itemstack to inventory

        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Check if player is editing a NewKit, and if so, if the kit has content
        if (EditKits.newKits.containsKey(player.getUniqueId())) {
            if(EditKits.newKits.get(player.getUniqueId()).getKitContent() != null) {
                // NewKit has content
                NewKit newKit = EditKits.newKits.get(player.getUniqueId());
                // Load kit contents
                ItemStack[] content = newKit.getKitContent();

                // Count
                int count = 0;
                // Set items to inventory
                for (ItemStack item : content) {
                    inventory.setItem(count, item);
                    count++;
                }
            }
            // Else statement is unnecessary, because the kit is empty
        } else {
            // Load kit content
            String kitName = EditKits.selectedKit.get(player.getUniqueId());

            List<ItemStack> kit = new ArrayList<>();

            try {
                // Loop through each item in the 'items' section of a kit
                for (String item : kitFile.get().getConfigurationSection("kits." + kitName + ".items").getKeys(false)) {
                    kit.add(new ItemStack(Material.getMaterial(item), kitFile.get().getInt("kits." + kitName + ".items." + item + ".amount")));
                }
            } catch (NullPointerException exception) {
                // Leave it empty, it's just a handler
            }

            try {
                // Loop through each item in the 'gear' section of a kit
                for (String gearItem : kitFile.get().getConfigurationSection("kits." + kitName + ".gear").getKeys(false)) {
                    String value = kitFile.get().getString("kits." + kitName + ".gear." + gearItem);
                    kit.add(new ItemStack(Material.getMaterial(value), 1));
                }
            } catch (NullPointerException exception) {
                // Leave it empty, it's just a handler
            }


            // Give kit
            ItemStack[] kitItems = kit.toArray(new ItemStack[0]);

            // Save current items
            currentItems.put(player.getUniqueId(), kitItems);

            // Count
            int count = 0;
            // Set items to inventory
            for (ItemStack item : kitItems) {
                inventory.setItem(count, item);
                count++;
            }
        }


        // Open inventory to the player
        player.openInventory(inventory);

        // Set this inventory as open inventory in the EditKits class
        EditKits.currentInventory.put(player.getUniqueId(), "editkitcontent");
    }
}
