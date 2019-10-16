package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {

    public PlayerPickupItem() {

    }

    @EventHandler
    public void onDrop(PlayerPickupItemEvent event) {
        // Player who picked up a item
        Player player = event.getPlayer();

        // If a player is in a setup, the player shouldn't be able to pick up a item
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
