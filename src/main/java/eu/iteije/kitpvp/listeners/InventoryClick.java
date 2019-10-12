package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.OpenInventory;
import eu.iteije.kitpvp.utils.editkits.inventories.KitsOverviewInventory;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventoryClick implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public InventoryClick(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Player who clicked in their inventory
        Player player = (Player) event.getWhoClicked();
        // Inventory the action is executed in
        Inventory inventory = event.getClickedInventory();

        // If a player is in a setup, the player is not able to interact with their inventory
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            // Close inventory, so players in creative can't double click
            player.closeInventory();
        }

        // If select menu inventory is opened, cancel event
        if (SelectKit.hasInventoryOpen.containsKey(player.getUniqueId())) {
            // Check whether the clicked inventory slot is a allowed kit slot
            if (Arrays.stream(SelectKit.allowedSlots).anyMatch(i -> i == event.getSlot())) {
                // If player is interacting with top inventory -> proceed
                if (inventory == event.getView().getTopInventory()) {
                    // Check if there is any kit in the slot
                    if (event.getCurrentItem().hasItemMeta()) {
                        // Cancel event, because a other class is triggered so the cancel event is unreachable from here (prevent NullPointerException)
                        event.setCancelled(true);
                        // Kit name
                        String kitName = TransferMessage.removeColors(event.getCurrentItem().getItemMeta().getDisplayName());
                        // Head back to SelectKit class to give the kit
                        SelectKit selectKit = new SelectKit(player, SelectKit.hasInventoryOpen.get(player.getUniqueId()));
                        selectKit.giveKit(player, kitName);
                    }
                }
            }

            // If clicked slot is 49 (close menu item) close selectkit inventory
            if (event.getSlot() == 49) {
                player.closeInventory();
            }
            event.setCancelled(true);
        }

        // Check whether a EditKit inventory is opened
        if (EditKits.currentInventory.containsKey(player.getUniqueId())) {
            // Cancel event here, because it could be possible the code never reaches this statement if placed at the end
            event.setCancelled(true);

            // Check whether the clicked inventory slot is a allowed kit slot
            if (Arrays.stream(SelectKit.allowedSlots).anyMatch(i -> i == event.getSlot())) {
                // Check whether the clicked item is a existing kit or a available kit slot
                if (!event.getCurrentItem().toString().equals(KitsOverviewInventory.undefinedKitItem.toString())) {
                    // The clicked item is considered a existing kit

                    // Kit name
                    String kitName = TransferMessage.removeColors(event.getCurrentItem().getItemMeta().getDisplayName());

                    // Open SelectKitActionInventory
                    OpenInventory openInventory = new OpenInventory("selectkitaction", player);
                    openInventory.setSelectedKit(kitName);
                    openInventory.openInventory();
                } else if (event.getCurrentItem().toString().equals(KitsOverviewInventory.undefinedKitItem.toString())) {
                    // The clicked item is considered a available kit slot

                }
            }
            // Continue here

            // Check if the player is interacting with the top inventory
            if (inventory == event.getView().getTopInventory()) {
                // In every single inventory the 'Close menu' button is at item slot 49, so if that slot is clicked, close menu
                if (event.getSlot() == 49) {
                    player.closeInventory();
                }
            }
        }
    }
}