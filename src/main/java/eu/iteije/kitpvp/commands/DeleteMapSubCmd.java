package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class DeleteMapSubCmd {

    // Instance of main class
    private KitPvP instance;

    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp deletemap <mapnaam>"
    );
    private List<String> explanation = Arrays.asList(
            "Verwijder een map"
    );

    // Help class instance
    private Help help = new Help(commands, explanation);

    // Maps.yml
    private MapFile mapFile;

    /**
     * @param instance instance of KitPvP (main) class
     */
    public DeleteMapSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     * @param args given arguments from the original command
     */
    public void send(CommandSender sender, String[] args) {
        // Define MapFile instance
        mapFile = new MapFile(instance, false);
        // Check permissions of sender
        if (sender.hasPermission("kitpvp.admin.deletemap")) {
            if (args.length == 2) {
                // Check whether the map exists or not
                if (mapFile.get().contains("maps." + args[1].toUpperCase())) {
                    // Remove map data
                    mapFile.get().set("maps." + args[1].toUpperCase(), null);
                    mapFile.save();
                    // Send success message
                    String message = Message.get("deletemap_success");
                    message = Message.replace(message, "{map}", args[1].toUpperCase());
                    Message.sendToSender(sender, message, true);
                } else {
                    // Send map exists error
                    String message = Message.get("deletemap_exists_error");
                    message = Message.replace(message, "{map}", args[1].toUpperCase());
                    Message.sendToSender(sender, message, true);
                }
            } else {
                // Help/wrong usage
                help.send(sender);
            }
        } else {
            // Permission error
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }
    }
}
