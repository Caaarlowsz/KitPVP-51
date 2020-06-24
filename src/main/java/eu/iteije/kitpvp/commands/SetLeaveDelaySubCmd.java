package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SetLeaveDelaySubCmd {

    // Instance of main class
    private KitPvP instance;


    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp setleavedelay <delay in seconds>"
    );
    private List<String> explanation = Arrays.asList(
            "Modify leave delay"
    );

    // Help class / CreateMap instances
    private Help help = new Help(commands, explanation);

    /**
     * @param instance instance of KitPvP (main) class
     */
    public SetLeaveDelaySubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender, String[] args) {
        // Check sender permissions
        if (sender.hasPermission("kitpvp.admin.setleavedelay")) {
            // Check whether the amount of arguments is correct
            if (args.length == 2) {
                if (isInt(args[1])) {
                    int newDelay = Integer.parseInt(args[1]);
                    // If new delay would be lower than 0, send error message
                    if (newDelay < 0) {
                        Message.sendToSender(sender, Message.get("setleavedelay_negative_number"), true);
                    } else {
                        // Instance of ConfigFile
                        ConfigFile configFile = new ConfigFile(instance, false);
                        int currentDelay = configFile.get().getInt("game_leave_delay");
                        // If the old delay is not the same as the new delay, proceed
                        if (currentDelay != newDelay) {
                            // Set new delay value
                            configFile.get().set("game_leave_delay", Integer.parseInt(args[1]));
                            configFile.save();
                            // Send success message
                            String message = Message.get("setleavedelay_success");
                            message = Message.replace(message, "{seconds}", args[1]);
                            Message.sendToSender(sender, message, true);
                        } else {
                            // Already *given* seconds error
                            String message = Message.get("setleavedelay_no_change");
                            message = Message.replace(message, "{seconds}", String.valueOf(currentDelay));
                            Message.sendToSender(sender, message, true);
                        }
                    }

                } else {
                    // Send parse error
                    Message.sendToSender(sender, Message.get("setleavedelay_parse_error"), true);
                }
            } else {
                // Send help page
                help.send(sender);
            }
        } else {
            // Permission error
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }
    }

    /**
     * @param input input which has to be checked
     * @return whether the input is a string or not
     */
    private boolean isInt(String input) {
        // Try parsing input to int
        try {
            Integer.parseInt(input);
            // Return true can parse the input to a int
            return true;
        } catch (Exception exception) {
            // If it doesn't work, return false
            return false;
        }
    }
}
