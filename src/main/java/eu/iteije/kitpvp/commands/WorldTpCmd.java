package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class WorldTpCmd implements CommandExecutor {

    // Instance of main class
    private KitPvP instance;

    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/worldtp <worldname> <x> <y> <z>"
    );
    private List<String> explanation = Arrays.asList(
            "Teleporteer naar een andere map"
    );

    // Help class instance
    private Help help = new Help(commands, explanation);

    /**
     * @param instance instance of KitPvP (main) class
     */
    public WorldTpCmd(KitPvP instance) {
        this.instance = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only players are allowed to use this command (console can't teleport, can it?)
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check player permissions
            if (player.hasPermission("kitpvp.admin.worldtp")) {
                // Required arguments: worldname, x, y, z (4)
                if (args.length == 0) {
                    // Send help if no arguments provided
                    help.send(sender);
                } else if (args.length == 4) {
                    // Try teleporting player to given world
                    try {
                        // Try making a new Location, throw exception if world doesn't exists or x/y/z aren't numbers
                        World world = Bukkit.getWorld(args[0]);
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        Location location = new Location(world, x, y, z);

                        // Teleport player to location
                        player.teleport(location);

                        // Teleport confirmation
                        String message = Message.get("worldtp_success");
                        message = Message.replace(message, "{world}", player.getLocation().getWorld().getName());
                        Message.sendToPlayer(player, message, true);
                    } catch (Exception exception) {
                        // Unable to teleport
                        // Temp message
                        Message.sendToPlayer(player, "&fTeleporting failed!", true);
                    }
                } else {
                    help.sendWrongUsage(sender, "/worldtp <worldname> <x> <y> <z>");
                }
            } else {
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }

        } else {
            // Player only
        }
        return true;
    }
}
