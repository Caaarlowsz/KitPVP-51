package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnSubCmd {

    // Instance of main class
    private KitPvP instance;

    // No help page for this command

    /**
     * @param instance instance of KitPvP (main) class
     */
    public SpawnSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Command executor have to be a player
        if (sender instanceof Player) {
            // Player as sender
            Player player = (Player) sender;


            try {
                // Return inventory/leave if player is in game
                if (Game.playersInGame.containsKey(player.getUniqueId())) {
                    // Leave game with specified delay in config.yml
                    Game.delayedLeave(player, true);
                } else {
                    // Try teleporting to spawn
                    teleportToSpawn(player);
                    // Send success message
                    Message.sendToPlayer(player, Message.get("spawn_success"), true);
                }

            } catch (Exception exception) {
                // Send fail message
                Message.sendToPlayer(player, Message.get("spawn_failed"), true);
            }

        } else {
            // Player only
            Message.sendToConsole(Message.PLAYER_ONLY, true);
        }
    }

    public void teleportToSpawn(Player player) {
        // Call getSpawn() method, which returns the location of the spawn
        Location spawn = getSpawn();

        // Teleport player to spawn
        player.teleport(spawn);
    }

    public boolean getSpawnSet() {
        try {
            // Instance of ConfigFile
            ConfigFile configFile = new ConfigFile(instance, false);

            if (configFile.get().contains("spawn")) return true;
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    public Location getSpawn() {
        // Instance of ConfigFile
        ConfigFile configFile = new ConfigFile(KitPvP.getInstance(), false);

        // Make up a new location based on data in config
        World world = Bukkit.getWorld(configFile.get().getString("spawn.world"));
        double x = (Double) configFile.get().get("spawn.x");
        double y = (Double) configFile.get().get("spawn.y");
        double z = (Double) configFile.get().get("spawn.z");
        float yaw = (Float) Float.parseFloat(configFile.get().getString("spawn.yaw"));
        float pitch = (Float) Float.parseFloat(configFile.get().getString("spawn.pitch"));
        Location spawn = new Location(world, x, y, z, yaw, pitch);

        return spawn;
    }
}
