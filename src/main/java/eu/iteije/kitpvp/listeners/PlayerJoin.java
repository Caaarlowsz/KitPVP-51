package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public PlayerJoin(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Try teleporting player to spawn if spawn_join = true
        try {
            // Instance of configFile
            ConfigFile configFile = new ConfigFile(instance, false);
            // Check whether the joining player has to be teleporter or not
            boolean spawnJoin = configFile.get().getBoolean("spawn_join");
            // If spawnJoin is true, try teleporting player to spawn
            if (spawnJoin) {
                // Instance of SpawnSubCmd
                SpawnSubCmd spawnSubCmd = new SpawnSubCmd(instance);
                spawnSubCmd.teleportToSpawn(player);
                // Send success message
                Message.sendToPlayer(player, Message.get("spawn_success"), true);
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            // Send failed message
            Message.sendToPlayer(player, Message.get("spawn_failed"), true);
        }
    }
}
