package eu.iteije.kitpvp.commands.subcommands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SetCenterSubCmd {

    private final PluginFile mapFile;
    private final KitPvP instance;

    public SetCenterSubCmd() {
        this.instance = KitPvP.getInstance();
        this.mapFile = instance.getMapFile();
    }

    public void send(CommandSender sender, String[] args) {
        if (sender.hasPermission("kitpvp.admin.setcenter")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 2) {
                    args[1] = args[1].toUpperCase();
                    if (mapFile.get().contains("maps." + args[1])) {
                        ConfigurationSection centerData = mapFile.get().getConfigurationSection("maps." + args[1]);
                        if (centerData == null) return;
                        Location location = player.getLocation();
                        centerData.set("center.world", location.getWorld().getName());
                        centerData.set("center.x", location.getX());
                        centerData.set("center.y", location.getY());
                        centerData.set("center.z", location.getZ());
                        centerData.set("center.pitch", location.getPitch());
                        centerData.set("center.yaw", location.getYaw());
                        mapFile.save();

                        Message.sendToPlayer(player, "&aCenter location has been updated!", false);
                    } else {
                        String message = Message.get("placenpc_map_error"); // just use the same message, no need to add another message line
                        message = Message.replace(message, "{map}", args[1]);
                        Message.sendToPlayer(player, message, true);
                    }
                } else {
                    Message.sendToSender(sender, "Invalid arguments", true);
                }
            } else {
                Message.sendToSender(sender, Message.PLAYER_ONLY, true);
            }
        } else {
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }
    }

}
