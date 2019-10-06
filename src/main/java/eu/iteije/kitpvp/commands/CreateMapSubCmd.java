package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CreateMapSubCmd {

    // Instance of main class
    private KitPvP instance;

    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp createmap <mapnaam>"
    );
    private List<String> explanation = Arrays.asList(
            "Maak een nieuwe map in je huidige wereld"
    );

    // Help class instance
    private Help help = new Help(commands, explanation);
    private CreateMap createMap;

    /**
     * @param instance instance of KitPvP (main) class
     */
    public CreateMapSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor (which in this case, only would be a player)
     * @param args given arguments from the original command
     */
    public void send(CommandSender sender, String[] args) {
        // Define createMap (instance of CreateMap)
        createMap = new CreateMap(instance);
        // Command executor has to be a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check player permissions
            if (player.hasPermission("kitpvp.admin.createmap")) {
                // Check list length of args
                if (args.length == 2) {
                    // Make sure map name is less or equal to 15
                    if (args[1].length() <= 15) {
                        if (!CreateMap.savedInventories.containsKey(player.getUniqueId())) {
                            // Creating new map message
                            String message = Message.get("createmap_command_success");
                            message = Message.replace(message, "{mapname}", args[1].toUpperCase());
                            Message.sendToPlayer(player, message, true);
                            // Starting new map setup
                            createMap.startSetup(player, args);
                        } else {
                            // Already in setup error
                            Message.sendToPlayer(player, Message.get("createmap_in_setup_error"), true);
                        }
                    } else {
                        // Map name too long error
                        Message.sendToPlayer(player, Message.get("createmap_name_length_error"), true);
                    }
                } else {
                    // Help/wrong usage
                    help.send(sender);
                }
            } else {
                // Permission error
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }
        } else {
            // Player only error
            Message.sendToConsole(Message.PLAYER_ONLY, true);
        }
    }


}
