package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditKitsSubCmd {

    // Instance of main class
    private KitPvP instance;

    // No help page for this command

    /**
     * @param instance instance of KitPvP (main) class
     */
    public EditKitsSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Check if the sender is of type Player
        if (sender instanceof Player) {
            // Define player (sender)
            Player player = (Player) sender;
            // Check player perms
            if (player.hasPermission("kitpvp.admin.editkits")) {
                // Create new EditKits object
                EditKits editKits = new EditKits(player);
                // Open KitsOverviewInventory
                editKits.openInventory("kitsoverview");
            } else {
                // No permission error
                Message.sendToPlayer(player, Message.PERMISSION_ERROR, true);
            }
        } else {
            // Player only command
            Message.sendToSender(sender, Message.PLAYER_ONLY, true);
        }
    }
}
