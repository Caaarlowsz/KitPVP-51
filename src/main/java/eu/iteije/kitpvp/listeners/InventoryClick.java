package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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

        // If a player is in a setup, the player is not able to interact with their inventory
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            // Close inventory, so players in creative can't double click
            player.closeInventory();
        }
    }
}