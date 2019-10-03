package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
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
        // Command executor has to be a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Check player permissions
            if (player.hasPermission("kitpvp.admin.createmap")) {
                // Check list length of args
                if (args.length == 2) {
                    if (args[1].length() <= 15) {
                        // Temp message
                        Message.sendToPlayer(player, "&fEen map maken met de naam " + args[1].toUpperCase() + "...", true);
                    } else {
                        // Map name too long error
                        // Temp message
                        Message.sendToPlayer(player, "&fDe naam van de map mag maximaal 15 letters of cijfers bevatten!", true);
                    }
                } else {
                    // Wrong usage

                    // Temp
                    help.send(sender);
                }
            } else {
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }
        } else {
            // Player only
        }
    }


}
