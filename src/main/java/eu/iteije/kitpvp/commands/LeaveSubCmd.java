package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveSubCmd {

    public LeaveSubCmd() {}

    /**
     * @param sender command executor
     */
    public void send(CommandSender sender) {
        // Check whether the player is in game (Game.playersInGame)
        if (sender instanceof Player) {
            // Command executor
            Player player = (Player) sender;
            if (Game.playersInGame.containsKey(player.getUniqueId())) {
                // Leave game with and return inventory
                Game.delayedLeave(player, true);
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
