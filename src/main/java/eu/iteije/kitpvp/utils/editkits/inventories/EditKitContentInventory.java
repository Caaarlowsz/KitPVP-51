package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditKitContentInventory {

    // Inventory title
    public static String EDIT_KIT_CONTENT_TITLE = TransferMessage.replaceColorCodes("&c&lBewerk de items");

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


        // Open inventory to the player
        player.openInventory(inventory);

        // Set this inventory as open inventory in the EditKits class
        EditKits.currentInventory.put(player.getUniqueId(), "editkitcontent");
    }
}
