package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.listeners.EntityDamageByEntity;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCmd implements CommandExecutor {

    public KillCmd() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("kitpvp.suicide")) {
                if (Game.playersInGame.containsKey(player.getUniqueId())) {
                    if (EntityDamageByEntity.outOfCombat(player.getUniqueId(), 20)) {
                        Game.leave(player);
                    } else {
                        Message.sendToPlayer(player, Message.get("leave_in_combat"), false);
                    }
                }
            }
        }

        return true;
    }
}
