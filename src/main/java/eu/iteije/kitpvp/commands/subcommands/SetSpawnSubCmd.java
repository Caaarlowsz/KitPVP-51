package eu.iteije.kitpvp.commands.subcommands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnSubCmd {

    public SetSpawnSubCmd() {}

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Define ConfigFile instance
        // Config.yml
        PluginFile configFile = KitPvP.getInstance().getConfigFile();
        // Command executor have to be a player
        if (sender instanceof Player) {
            // Check permissions
            Player player = (Player) sender;
            if (player.hasPermission("kitpvp.admin.setspawn")) {
                Location location = player.getLocation();
                // Try setting spawn data to config
                try {
                    configFile.get().set("spawn.world", location.getWorld().getName());
                    configFile.get().set("spawn.x", location.getX());
                    configFile.get().set("spawn.y", location.getY());
                    configFile.get().set("spawn.z", location.getZ());
                    configFile.get().set("spawn.pitch", location.getPitch() + "f");
                    configFile.get().set("spawn.yaw", location.getYaw() + "f");
                    configFile.save();

                    // Send success message
                    Message.sendToPlayer(player, Message.get("setspawn_success"), true);
                } catch (Exception exception) {
                    // Spawnpoint failed error
                    Message.sendToPlayer(player, Message.get("setspawn_failed"), true);
                }
            } else {
                // Permissions error
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }
        } else {
            // Player only
            Message.sendToConsole(Message.PLAYER_ONLY, true);
        }
    }

}
