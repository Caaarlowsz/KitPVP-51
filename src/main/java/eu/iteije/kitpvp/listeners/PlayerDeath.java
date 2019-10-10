package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public PlayerDeath(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Player who died
        Player player = event.getEntity();

        // If player is killed while ingame, clear drops
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            // Clear item drops
            event.getDrops().clear();

            // Get killer
            Player killer = player.getKiller();

            // Set death message to null / remove death message
            event.setDeathMessage(null);

            // Check whether the player is killed by a player or not
            if (killer != null) {
                // Convert death message
                String message = Message.get("game_death_by_player");
                message = Message.replace(message, "{target}", player.getName());
                message = Message.replace(message, "{killer}", killer.getName());

                // Broadcast death message
                Message.broadcast(message, true);
            } else {
                // Convert death message
                String message = Message.get("game_death_unknown_cause");
                message = Message.replace(message, "{target}", player.getName());

                // Broadcast death message
                Message.broadcast(message, true);
            }
        }
    }
}
