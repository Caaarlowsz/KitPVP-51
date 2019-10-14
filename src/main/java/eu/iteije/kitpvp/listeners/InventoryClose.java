package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.game.SelectKit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public InventoryClose(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Player who close a inventory
        Player player = (Player) event.getPlayer();

        // Check whether the opened inventory is the select kit inventory
        // If so, remove the player from the HashMap
        if (SelectKit.hasInventoryOpen.containsKey(player.getUniqueId())) {
            // Remove player from HashMap
            SelectKit.hasInventoryOpen.remove(player.getUniqueId());
            return;
        }

        // Remove player from EditKits.currentInventory if it was in a EditKit inventory
        if (EditKits.currentInventory.containsKey(player.getUniqueId())) {
            // Remove player from HashMap
            EditKits.currentInventory.remove(player.getUniqueId());
        }
    }
}
