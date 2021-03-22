package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceNpcSubCmd {

    private final KitPvP instance;

    public PlaceNpcSubCmd() {
        this.instance = KitPvP.getInstance();
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender, String[] args) {
        if (sender.hasPermission("kitpvp.admin.placenpc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                instance.getNpcModule().placeNpc(args[1], player.getLocation(), true);
            } else {
                Message.sendToSender(sender, Message.PLAYER_ONLY, true);
            }
        } else {
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }
    }
}
