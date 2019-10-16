package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItems implements Listener {

    public PlayerSwapHandItems() {

    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        // Player who switches between main/off hand
        Player player = event.getPlayer();

        // Cancel event to prevent players moving around their items making the server think they're using normal blocks (instead of setup blocks)
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
