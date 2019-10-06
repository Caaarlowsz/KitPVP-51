package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public PlayerDropItem(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        // Player who dropped a item
        Player player = event.getPlayer();

        // If a player is in a setup, the player is not able to drop a item
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }

        // If a player is in game, the player isnot able to drop a item
        if (Game.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
