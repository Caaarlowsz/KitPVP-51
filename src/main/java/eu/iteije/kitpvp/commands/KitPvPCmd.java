package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class KitPvPCmd implements CommandExecutor {

    // Instance of main class
    private KitPvP instance;

    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp createmap",
            "/kitpvp maps"
    );
    private List<String> explanation = Arrays.asList(
            "Create a map",
            "Get a list of maps"
    );

    // Help class instance
    private Help help = new Help(commands, explanation);

    /**
     * @param instance instance of KitPvP (main) class
     */
    public KitPvPCmd(KitPvP instance) {
        this.instance = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If CommandSender entered 0 arguments, global help list will show up
        if (args.length == 0) {
            help.send(sender);
        } else {
            // Check first argument and redirect to associated class
            switch (args[0]) {
                case "createmap":
                    CreateMapSubCmd createMapSubCmd = new CreateMapSubCmd(instance);
                    createMapSubCmd.send(sender, args);
                    break;
                default:
                    help.send(sender);
                    break;
            }
        }
        return true;
    }
}
