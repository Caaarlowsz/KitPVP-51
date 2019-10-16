package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.InventoryItem;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.NewKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EditKitInventory {

    // Inventory title
    public static String EDIT_KIT_INVENTORY_TITLE = TransferMessage.replaceColorCodes("&c&lBewerk de kit");

    // Undefined player variable
    private Player player;

    /**
     * @param player player the inventory will be opened for
     */
    public EditKitInventory(Player player) {
        this.player = player;
    }

    public void open() {
        // New instance of Inventory, setting the size to 54 slots and the title
        Inventory inventory = Bukkit.createInventory(null, 54, EDIT_KIT_INVENTORY_TITLE);

        // Close menu item
        ItemStack closeItem = InventoryItem.createItem(Material.BARRIER, 1, "&cSluit menu"); // create new itemstack
        inventory.setItem(49, closeItem); // assign itemstack to inventory

        // Return to previous menu item
        ItemStack returnItem = InventoryItem.createItem(Material.SPECTRAL_ARROW, 1, "&cGa terug"); // create new itemstack
        inventory.setItem(45, returnItem); // assign itemstack to inventory

        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Selected kit name
        String selectedKit;
        // Selected kit icon
        String selectedKitIcon;

        if (EditKits.newKits.containsKey(player.getUniqueId())) {
            NewKit newKit = EditKits.newKits.get(player.getUniqueId());

            selectedKit = newKit.getKitName();
            selectedKitIcon = newKit.getKitIconAsString();
        } else {
            selectedKit = EditKits.selectedKit.get(player.getUniqueId());
            selectedKitIcon = Message.replace(kitFile.get().getString("kits." + selectedKit + ".icon").toLowerCase(), "_", " ");
        }

        // Name tag item (which shows the name of the selected kit)
        ItemStack kitNameItem = InventoryItem.createItem(Material.NAME_TAG, 1, "&cHuidige naam: &f" + selectedKit,
                Arrays.asList(TransferMessage.replaceColorCodes("&7Klik om te bewerken")));
        inventory.setItem(20, kitNameItem);

        // Item frame item (which shows the current kit icon)
        ItemStack kitIconItem = InventoryItem.createItem(Material.ITEM_FRAME, 1, "&cHuidig icoon: &f" +
                selectedKitIcon,
                Arrays.asList(TransferMessage.replaceColorCodes("&7Klik om te bewerken")));
        inventory.setItem(22, kitIconItem);

        // Check (shows nothing)
        ItemStack kitContentsItem = InventoryItem.createItem(Material.CHEST, 1, "&cBewerk de items",
                Arrays.asList(TransferMessage.replaceColorCodes("&7Klik om te bewerken")));
        inventory.setItem(24, kitContentsItem);


        // Open inventory
        player.openInventory(inventory);

        // Set this inventory as open inventory in the EditKits class
        EditKits.currentInventory.put(player.getUniqueId(), "editkit");

        // Add finish item to new kit inventory if everything is set
        if (EditKits.newKits.containsKey(player.getUniqueId())) {
            NewKit newKit = EditKits.newKits.get(player.getUniqueId());

            // Set 'finish' item if everything is set
            KitPvP.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(KitPvP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (newKit.isAllSet()) {
                        // New spectral arrow ItemStack
                        ItemStack finishItem = InventoryItem.createItem(Material.SPECTRAL_ARROW, 1, "&cVoeg kit toe");
                        inventory.setItem(53, finishItem);
                    }
                }
            }, 20L); // wait 1 sec so previous running methods can be finished
        }
    }
}
