package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ModifyHungerSubCmd {

    // Instance of main class
    private KitPvP instance;


    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp modifyhunger <aan/uit>"
    );
    private List<String> explanation = Arrays.asList(
            "Zet honger in game aan of uit"
    );

    // Help class / CreateMap instances
    private Help help = new Help(commands, explanation);

    /**
     * @param instance instance of KitPvP (main) class
     */
    public ModifyHungerSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender, String[] args) {
        // Check sender permissions
        if (sender.hasPermission("kitpvp.admin.modifyhunger")) {
            // Check whether the amount of arguments is correct
            if (args.length == 2) {
                // Check second argument
                if (args[1].toLowerCase().equals("aan") || args[1].toLowerCase().equals("uit")) {
                    // Instance of ConfigFile
                    ConfigFile configFile = new ConfigFile(instance, false);

                    // Original modification
                    boolean oldHunger = configFile.get().getBoolean("game_hunger");
                    // New hunger modification
                    boolean hunger = true;
                    if (args[1].toLowerCase().equals("aan")) hunger = true;
                    if (args[1].toLowerCase().equals("uit")) hunger = false;

                    if (oldHunger != hunger) {
                        // Modify hunger value
                        configFile.get().set("game_hunger", hunger);
                        configFile.save();
                        // Send success message
                        String message = Message.get("modifyhunger_success");
                        message = Message.replace(message, "{hunger}", args[1]);
                        Message.sendToSender(sender, message, true);
                    } else {
                        // Send no changes message
                        String message = Message.get("modifyhunger_no_change");
                        message = Message.replace(message, "{hunger}", args[1].toLowerCase());
                        Message.sendToSender(sender, message, true);
                    }
                } else {
                    // Incorrect arguments
                    Message.sendToSender(sender, Message.get("modifyhunger_wrong_args"), true);
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
}
