package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.CreateMap;
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
            "/worldtp <wereldnaam> <x> <y> <z>"
    );
    private List<String> explanation = Arrays.asList(
            "Teleporteer naar een andere map"
    );

    // Help class / CreateMap instances
    private Help help = new Help(commands, explanation);
    private CreateMap createMap = new CreateMap(instance);

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
                        teleport(player, location);

                        // Teleport confirmation
                        String message = Message.get("worldtp_success");
                        message = Message.replace(message, "{world}", player.getLocation().getWorld().getName());
                        Message.sendToPlayer(player, message, true);
                    } catch (Exception exception) {
                        // Unable to teleport / wrong usage
                        help.sendWrongUsage(sender, "/worldtp <wereldnaam> <x> <y> <z>");
                    }
                } else {
                    // Wrong usage
                    help.sendWrongUsage(sender, "/worldtp <wereldnaam> <x> <y> <z>");
                }
            } else {
                // Permission error
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }

        } else {
            // Player only
            Message.sendToConsole(Message.PLAYER_ONLY, true);
        }
        return true;
    }

    public void teleport(Player player, Location location) {
        try {
            // Try teleporting player to given destination
            player.teleport(location);
        } catch (Exception exception) {
            // Unable to teleport / wrong usage
            help.sendWrongUsage(player, "/worldtp <wereldnaam> <x> <y> <z>");
        }
    }
}
