package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
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
            // Check whether the slot is a allowed kit slot
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
            // Clicked slot: event.getSlot();
            // Current item: event.getCurrentItem();
            event.setCancelled(true);
        }
    }
}