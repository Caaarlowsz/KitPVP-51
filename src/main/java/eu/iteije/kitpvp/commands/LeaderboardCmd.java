package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.data.DataHandler;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class LeaderboardCmd implements CommandExecutor {

    // Instance of main class
    private KitPvP instance;


    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/leaderboard"
    );
    private List<String> explanation = Arrays.asList(
            "Top 10 players"
    );

    // Help class / CreateMap instances
    private Help help = new Help(commands, explanation);
    private DataHandler dataHandler;

    /**
     * @param instance instance of KitPvP (main) class
     */
    public LeaderboardCmd(KitPvP instance) {
        this.instance = instance;
        this.dataHandler = new DataHandler();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only players are allowed to use this command (console can't teleport, can it?)

        // Check player permissions
        if (sender.hasPermission("kitpvp.leaderboard")) {

            DataHandler.getHandler().sendTop(sender);

        } else {
            // Permission error
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }


        return true;
    }


}
