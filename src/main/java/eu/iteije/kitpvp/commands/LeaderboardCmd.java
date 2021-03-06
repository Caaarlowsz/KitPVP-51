package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class LeaderboardCmd implements CommandExecutor {

    public LeaderboardCmd() {}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only players are allowed to use this command (console can't teleport, can it?)

        // Check player permissions
        if (sender.hasPermission("kitpvp.leaderboard")) {

            sendTop(sender);

        } else {
            // Permission error
            Message.sendToSender(sender, Message.PERMISSION_ERROR, true);
        }


        return true;
    }

    private void sendTop(CommandSender sender) {
        Map<String, Integer> leaderboard = UserCache.leaderboard;
        sendMessage(sender, "&3&lTop 10:");
        int count = 1;
        for (String user : leaderboard.keySet()) {
            sendMessage(sender, "&b" + count + "&7 - &b" + user + " &f(" + leaderboard.get(user) + " kills)");
            count++;
        }
    }

    // Why is this in the datahandler?
    private void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }


}
