package eu.iteije.kitpvp.pluginutils;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    // Instances of files
    private static final PluginFile configFile = KitPvP.getInstance().getConfigFile();
    private static final PluginFile messageFile = KitPvP.getInstance().getMessageFile();

    // Global messages
    public static String PERMISSION_ERROR = Message.get("permission_error");
    public static String WRONG_USAGE = Message.get("wrong_usage");
    public static String PLAYER_ONLY = Message.get("player_only");

    /**
     * @param player    target Player to send the message to
     * @param message   message which will be sent to the targeted player
     * @param hasPrefix if true, the plugin prefix will be applied to the message
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
     * @param message   message which will be sent to the console
     * @param hasPrefix if true, the plugin prefix will be applied to the message
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
     * @param sender    CommandSender (so it isn't necessary to use 'instanceof' over and over again)
     * @param message   message which will be sent to the sender
     * @param hasPrefix if true, the plugin prefix will be applied to the message
     */
    public static void sendToSender(CommandSender sender, String message, boolean hasPrefix) {
        // Call send methods of different senders
        if (sender instanceof Player) {
            sendToPlayer((Player) sender, message, hasPrefix);
        } else {
            sendToConsole(message, hasPrefix);
        }
    }

    /**
     * @param message   message which will be broadcasted
     * @param hasPrefix if true, the plugin prefix will be applied to the message
     */
    public static void broadcast(String message, boolean hasPrefix) {
        // Convert message
        if (hasPrefix) {
            message = configFile.get().getString("prefix") + message;
        }

        // Replace color codes
        message = TransferMessage.replaceColorCodes(message);

        // Broadcast message
        Bukkit.broadcastMessage(message);
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
     * @param message   message where a certain part has to be replaced from
     * @param oldString old String
     * @param newString String the old string will be replaced by
     * @return converted message
     */
    public static String replace(String message, String oldString, String newString) {
        return message.replace(oldString, newString);
    }

}
