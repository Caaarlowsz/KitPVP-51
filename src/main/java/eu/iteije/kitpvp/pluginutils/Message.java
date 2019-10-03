package eu.iteije.kitpvp.pluginutils;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

    // Instance of KitPvP (main) class
    private static KitPvP instance = KitPvP.getInstance();

    // Instances of files
    private static ConfigFile configFile = new ConfigFile(instance, false);
    private static MessageFile messageFile = new MessageFile(instance, false);

    // Global messages
    public static String PERMISSION_ERROR = Message.get("permission_error");
    public static String WRONG_USAGE = Message.get("wrong_usage");

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

        // Replace color codes
        message = TransferMessage.replaceColorCodes(message);

        // Send converted message to the target Player
        player.sendMessage(message);
    }

    /**
     * @param message message which will be sent to the console
     * @param hasPrefix if true, the plugin prefix is added before the message
     */
    public static void sendToConsole(String message, boolean hasPrefix) {
        // Convert message
        if (hasPrefix) {
            message = configFile.get().getString("prefix") + message;
        }

        // Replace color codes
        message = TransferMessage.replaceColorCodes(message);

        // Send converted message to the target; console
        Bukkit.getConsoleSender().sendMessage(message);
    }

    /**
     * @param messagePath name of specific String in messages.yml
     * @return String from messages.yml
     */
    public static String get(String messagePath) {
        String message = "";

        // Try retrieving message from messages.yml
        try {
            // Define String message by retrieved String from messages.yml
            message = messageFile.get().getString(messagePath);

            return message;
        } catch (NullPointerException exception) {
            // Define String message, so it won't return null
            message = "Path " + messagePath + " is invalid!";
            // Print stack trace
            exception.printStackTrace();

            return message;
        }
    }

    /**
     * @param message message where a certain part has to be replaced from
     * @param oldString old String
     * @param newString String the old string will be replaced by
     * @return converted message
     */
    public static String replace(String message, String oldString, String newString) {
        return message.replace(oldString, newString);
    }

}
