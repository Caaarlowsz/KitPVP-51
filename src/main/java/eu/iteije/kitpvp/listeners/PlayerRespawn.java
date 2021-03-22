package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public PlayerRespawn(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Player who is respawning
        Player player = event.getPlayer();

        // If player is respawning while in game, proceed
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect clear " + player.getName());

            // Let the player leave
            Game.leave(player);
            // Set respawn location to the lobby spawn
            SpawnSubCmd spawnSubCmd = new SpawnSubCmd();
            event.setRespawnLocation(spawnSubCmd.getSpawn());
        }
    }
}
