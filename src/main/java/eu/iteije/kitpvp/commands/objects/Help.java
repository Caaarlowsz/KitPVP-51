package eu.iteije.kitpvp.commands.objects;

import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Help {

    // Commands and explanations will be saved here
    private final List<String> commands;
    private final List<String> explanation;

    /**
     * @param commands    commands given from a (sub)command class
     * @param explanation explanation of commands from a (sub)command class
     */
    public Help(List<String> commands, List<String> explanation) {
        this.commands = commands;
        this.explanation = explanation;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Check sender type, this is due to the Message class (sendToPlayer, sendToConsole)
        if (sender instanceof Player) {
            Message.sendToPlayer((Player) sender, "", false);
            Message.sendToPlayer((Player) sender, Message.get("help_title"), true);
        } else if (sender instanceof ConsoleCommandSender) {
            Message.sendToConsole("", false);
            Message.sendToConsole(Message.get("help_title"), true);
        }

        // Send the commands/explanations
        for (int i = 0; i < commands.size(); i++) {
            // Convert message into a valid help line
            String message = Message.get("help_line_layout");
            message = Message.replace(message, "{command}", commands.get(i));
            message = Message.replace(message, "{explanation}", explanation.get(i));

            // Check sender type, then send the converted message
            if (sender instanceof Player) {
                Message.sendToPlayer((Player) sender, message, true);
            } else if (sender instanceof ConsoleCommandSender) {
                Message.sendToConsole(message, true);
            }
        }
    }

    public void sendWrongUsage(CommandSender sender, String correctCommand) {
        // Define message
        String message = Message.WRONG_USAGE;
        message = Message.replace(message, "{command}", correctCommand);
        // Check sender type
        if (sender instanceof Player) {
            // Wrong usage message for a player
            Message.sendToPlayer((Player) sender, message, true);
        } else if (sender instanceof ConsoleCommandSender) {
            // Wrong usage message for console
            Message.sendToConsole(message, true);
        }
    }

}
