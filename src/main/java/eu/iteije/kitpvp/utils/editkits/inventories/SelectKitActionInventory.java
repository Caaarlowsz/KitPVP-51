package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SelectKitActionInventory {

    // Inventory title
    public static String EDIT_KIT_SELECT_KIT_ACTION_TITLE = TransferMessage.replaceColorCodes("&c&lSelecteer een actie");

    // Edit / delete kit item slots
    public static Integer[] editKitSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
    public static Integer[] deleteKitSlots = {14, 15, 16, 23, 24, 25, 32, 33, 34};

    // Undefined player variable
    private Player player;

    /**
     * @param player player the inventory will be opened for
     */
    public SelectKitActionInventory(Player player) {
        this.player = player;
    }

    public void open() {
        // New instance of Inventory, setting the size to 54 slots and the title
        Inventory inventory = Bukkit.createInventory(null, 54, EDIT_KIT_SELECT_KIT_ACTION_TITLE);

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cSluit menu"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        // Return to previous menu item
        ItemStack returnItem = InventoryItem.createItem(Material.ARROW, 1, "&cGa terug"); // create new itemstack
        inventory.setItem(45, returnItem); // assign itemstack to inventory

        // Edit/delete kit itemstacks
        ItemStack editKitItem = InventoryItem.createItem(new ItemStack(Material.GREEN_WOOL, 1).getType(), 1, "&aBewerk kit");
        ItemStack deleteKitItem = InventoryItem.createItem(new ItemStack(Material.RED_WOOL, 1).getType(), 1, "&cVerwijder kit");

        // Set all 'Edit kit' slots
        for (int slot : editKitSlots) {
            inventory.setItem(slot, editKitItem);
        }

        // Set all 'Delete kit' slots
        for (int slot : deleteKitSlots) {
            inventory.setItem(slot, deleteKitItem);
        }

        // Open inventory
        player.openInventory(inventory);

        // Set this inventory as open inventory in the EditKits class
        EditKits.currentInventory.put(player.getUniqueId(), "selectkitaction");
    }
}
