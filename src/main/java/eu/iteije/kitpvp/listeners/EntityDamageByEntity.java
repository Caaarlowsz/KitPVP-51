package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    public EntityDamageByEntity() {

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Define damager and damagetaker
        Entity damagerEntity = event.getDamager();
        Entity takerEntity = event.getEntity();

        // Make sure both entities are players
        if (takerEntity instanceof Player && damagerEntity instanceof Player) {
            Player damager = (Player) damagerEntity;
            Player taker = (Player) takerEntity;

            // If the either the damager and taker are in game, proceed
            if (Game.playersInGame.containsKey(taker.getUniqueId()) && Game.playersInGame.containsKey(damager.getUniqueId())) {
                // If damager is not in survival mode (if everything works fine, this wouldn't be possible) the event has to be cancelled
                if (damager.getGameMode() != GameMode.SURVIVAL) {
                    event.setCancelled(true);
                }

//                Check if players are in the same map
//                if (Game.playersInGame.get(taker.getUniqueId()).equals(Game.playersInGame.get(damager.getUniqueId()))) {
//                    // If damager is not in survival mode (if everything works fine, this wouldn't be possible) the event has to be cancelled
//                    if (damager.getGameMode() != GameMode.SURVIVAL) {
//                        event.setCancelled(true);
//                    }
//                } else {
//                    event.setCancelled(true);
//                }
            } else {
                // If damagetaker is ingame, but the damager isn't, cancel event
                event.setCancelled(true);
            }
        }
    }
}
