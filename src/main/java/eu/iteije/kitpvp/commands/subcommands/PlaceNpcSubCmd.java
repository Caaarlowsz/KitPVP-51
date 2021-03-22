package eu.iteije.kitpvp.commands.subcommands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceNpcSubCmd {

    private final KitPvP instance;
    private final PluginFile mapFile;

    public PlaceNpcSubCmd() {
        this.instance = KitPvP.getInstance();
        this.mapFile = instance.getMapFile();
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender, String[] args) {
        if (sender.hasPermission("kitpvp.admin.placenpc")) {
            if (sender instanceof Player) {
                if (args.length == 2) {
                    Player player = (Player) sender;
                    if (mapFile.get().contains("maps." + args[1])) {
                        instance.getNpcModule().placeNpc(args[1], player.getLocation(), true);
                    } else {
                        String message = Message.get("placenpc_map_error");
                        message = Message.replace(message, "{map}", args[1]);
                        Message.sendToPlayer(player, message, true);
                    }
                } else {
                    Message.sendToSender(sender, "Invalid arguments", true);
                }
            } else {
                Message.sendToSender(sender, Message.PLAYER_ONLY, true);
            }
        } else {
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }
    }
}
