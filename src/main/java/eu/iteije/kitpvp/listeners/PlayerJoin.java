package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.data.DataHandler;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.Scoreboard;
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

        // Check player data in database
        DataHandler.getHandler().loadPlayer(player.getUniqueId());

        // Try teleporting player to spawn if spawn_join = true
        try {
            // Instance of configFile
            ConfigFile configFile = new ConfigFile(instance, false);
            // Check whether the joining player has to be teleported or not
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

        // Wait a few seconds, to prevent NullPointerExceptions (it can take a few seconds to load player data)
        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> Scoreboard.load(player), 40L); // delay / 20 = seconds

    }
}
