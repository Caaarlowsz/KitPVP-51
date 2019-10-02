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
            "Create a map"
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
            // Check list length of args
            if (args.length == 2) {
                // Temp message
                // NOTE: max characters in a name should be 15!
                Message.sendToPlayer(player, "&fEen map maken met de naam " + args[1].toUpperCase() + "...", true);
            } else {
                // Wrong usage

                // Temp
                help.send(sender);
            }
        } else {
            // Player only
        }
    }


}
