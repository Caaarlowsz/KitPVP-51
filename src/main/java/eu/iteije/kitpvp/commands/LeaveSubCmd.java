package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveSubCmd {

    // Instance of main class
    private KitPvP instance;

    // No help page for this command

    /**
     * @param instance instance of KitPvP (main) class
     */
    public LeaveSubCmd(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Check whether the player is in game (Game.playersInGame)
        if (sender instanceof Player) {
            // Command executor
            Player player = (Player) sender;
            if (Game.playersInGame.containsKey(player.getUniqueId())) {
                // Leave game and return inventory
                Game.leave(player);
                // Success message
                Message.sendToPlayer(player, Message.get("leave_success"), true);
            } else {
                // Send fail message (if a player is not in a game but is using the command)
                Message.sendToPlayer(player, Message.get("leave_not_ingame"), true);
            }
        } else {
            // Player only error
            Message.sendToSender(sender, Message.PLAYER_ONLY, true);
        }

    }
}
