package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.game.SelectKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class KitsOverviewInventory {

    // Inventory title
    public static String EDIT_KIT_KITS_OVERVIEW_TITLE = TransferMessage.replaceColorCodes("&c&lOverzicht van de kits");

    // Undefined kit item
    public static ItemStack undefinedKitItem = InventoryItem.createItem(Material.ARROW, 1, "&aBeschikbare plek",
            Arrays.asList(TransferMessage.replaceColorCodes("&7Klik om te bewerken")));

    // Undefined player variable
    private Player player;

    /**
     * @param player player the inventory will be opened for
     */
    public KitsOverviewInventory(Player player) {
        this.player = player;
    }

    public void open() {
        // Remove player from selectedKit HashMap if it is in
        if (EditKits.selectedKit.containsKey(player.getUniqueId())) EditKits.selectedKit.remove(player.getUniqueId());
        // Remove player from newKits HashMap if it is in
        if (EditKits.newKits.containsKey(player.getUniqueId())) EditKits.newKits.remove(player.getUniqueId());

        // New instance of Inventory, setting the size to 54 slots and the title
        Inventory inventory = Bukkit.createInventory(null, 54, EDIT_KIT_KITS_OVERVIEW_TITLE);

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cSluit menu"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Put kits into inventory
        int count = 0;
        // Loop through all kits
        for (String kitName : kitFile.get().getConfigurationSection("kits").getKeys(false)) {
            // Check whether the amount kits in the inventory is not higher than the amount of allowed slots
            if (count < SelectKit.allowedSlots.length) {
                // Get icon material from config
                Material material = Material.getMaterial(kitFile.get().getString("kits." + kitName + ".icon").toUpperCase());
                // Create new ItemStack
                ItemStack kitItemStack = InventoryItem.createItem(material, 1, "&c" + kitName,
                        Arrays.asList(TransferMessage.replaceColorCodes("&7Klik om te bewerken")));

                // Assign itemstack to the next allowed slot
                inventory.setItem(SelectKit.allowedSlots[count], kitItemStack);
                count++;
            }
        }

        // Loop through all empty kit slots and set the undefined kit item
        for (int i = count; i < SelectKit.allowedSlots.length; i++) {
            // Assign undefined kit item to next allowed slot
            inventory.setItem(SelectKit.allowedSlots[i], undefinedKitItem);
        }

        // Open inventory
        player.openInventory(inventory);

        // Set this inventory as open inventory in the EditKits class
        EditKits.currentInventory.put(player.getUniqueId(), "kitsoverview");
    }
}
