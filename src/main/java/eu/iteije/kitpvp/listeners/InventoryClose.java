package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.game.SelectSpawn;
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
        }

        if (SelectSpawn.hasInventoryOpen.containsKey(player.getUniqueId())) {
            // Remove player from HashMap
            SelectSpawn.hasInventoryOpen.remove(player.getUniqueId());
        }
    }
}
