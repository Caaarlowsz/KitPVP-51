package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Tool {

    // Data needed for itemstack/itemmeta
    private final Material material;
    private final int inventorySlot;
    private final String displayName;

    /**
     * @param material      material of the tool
     * @param inventorySlot slot the tool has to be in if calling setToInventory()
     * @param displayName   display name of the tool
     */
    public Tool(Material material, int inventorySlot, String displayName) {
        this.material = material;
        this.inventorySlot = inventorySlot;
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    /**
     * @return tool as ItemStack
     */
    private ItemStack getItemStack() {
        // Creating new ItemStack
        ItemStack itemStack = new ItemStack(material, 1);
        // Get item meta of itemStack
        ItemMeta itemMeta = itemStack.getItemMeta();
        // Try setting the tool name, since displayName can be null, placed in a try statement
        try {
            // Set display name to itemmeta
            itemMeta.setDisplayName(TransferMessage.replaceColorCodes(displayName));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        // Apply the updated itemmeta to the itemstack
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    // Set item to inventory
    public void setToInventory(Player player) {
        player.getInventory().setItem(inventorySlot, getItemStack());
    }
}
