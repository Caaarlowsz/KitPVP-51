package eu.iteije.kitpvp.pluginutils;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

    private static KitPvP instance = KitPvP.getInstance();
    private static ConfigFile configFile = new ConfigFile(instance, false);
    /**
     * @param player target Player to send the message to
     * @param message message which will be sent to the targeted player
     * @param hasPrefix if true, a plugin prefix is added before the message
     */
    public static void sendToPlayer(Player player, String message, boolean hasPrefix) {
        // Convert message
        if (hasPrefix) {
            message = configFile.get().getString("prefix") + message;
        }

        message = TransferMessage.replaceColorCodes(message);


        player.sendMessage(message);
    }

    /**
     * @param message message which will be sent to the console
     */
    public static void sendToConsole(String message, boolean hasPrefix) {
        // Convert message
        if (hasPrefix) {
            message = configFile.get().getString("prefix") + message;
        }

        message = TransferMessage.replaceColorCodes(message);

        Bukkit.getConsoleSender().sendMessage(message);
    }

}
