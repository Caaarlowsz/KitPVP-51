package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.data.DataHandler;
import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.Scoreboard;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    public PlayerDeath() {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Player who died
        Player player = event.getEntity();

        // If player is killed while in game, clear drops
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            // Clear item drops
            event.getDrops().clear();

            // Get killer
            Player killer = player.getKiller();

            // Set death message to null / remove death message
            event.setDeathMessage(null);

            // Instance of DataHandler
            DataHandler dataHandler = new DataHandler();

            // Check whether the player is killed by a player or not
            if (killer != null) {
                // Convert death message
                String message = Message.get("game_death_by_player");
                message = Message.replace(message, "{target}", player.getName());
                message = Message.replace(message, "{killer}", killer.getName());
                message = Message.replace(message, "{health}", String.format("%.2f", (killer.getHealth() / 2)));

                // Broadcast death message
                Message.broadcast(message, false);

                killer.setHealth(20);

                // Add kill to the killer and a death to the target
                dataHandler.addKill(killer.getUniqueId());
                dataHandler.addDeath(player.getUniqueId());

                // Save the kill/death to UserCache
                UserCache.updateKills(killer.getUniqueId(), 1);
                UserCache.updateDeaths(player.getUniqueId(), 1);

                // Update scoreboard
                Scoreboard.load(killer);
                Scoreboard.load(player);
            } else {
                // Convert death message
                String message = Message.get("game_death_unknown_cause");
                message = Message.replace(message, "{target}", player.getName());

                // Broadcast death message
                Message.broadcast(message, false);

                // Add a death to the target
                dataHandler.addDeath(player.getUniqueId());

                // Save the death to UserCache
                UserCache.updateDeaths(player.getUniqueId(), 1);

                // Update scoreboard
                Scoreboard.load(player);
            }
        }
    }
}
