package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityDamageByEntity implements Listener {

    private static Map<UUID, Long> combatLog; // bad practice

    public EntityDamageByEntity() {
        combatLog = new HashMap<>();
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

                // Combat log
                combatLog.put(damager.getUniqueId(), System.currentTimeMillis());
                combatLog.put(taker.getUniqueId(), System.currentTimeMillis());
            } else {
                // If damagetaker is ingame, but the damager isn't, cancel event
                event.setCancelled(true);
            }
        }
    }

    /**
     * @param uuid uuid of the player to check combat activity for
     * @param delay time in seconds the player has to be out of combat
     * @return whether the last registered combat activity is past the certain period
     */
    public static boolean outOfCombat(UUID uuid, int delay) {
        return System.currentTimeMillis() - (delay * 1000L) >= combatLog.getOrDefault(uuid, 0L);
    }

    public static void removeCombatLog(UUID uuid) {
        combatLog.remove(uuid);
    }
}
