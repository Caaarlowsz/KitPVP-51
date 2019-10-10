package eu.iteije.kitpvp.pluginutils;

import org.bukkit.ChatColor;

public class TransferMessage {

    /**
     * @param message message with unconverted color codes
     * @return converted String
     */
    public static String replaceColorCodes(String message) {
        // I don't return the String immediately because if I would add more color codes, it is hard to read

        // Replace & with color code character §
        message = message.replace('&', '§');

        return message;
    }

    /**
     * @param message string which has to be converted
     * @return string without colors
     */
    public static String removeColors(String message) {
        return ChatColor.stripColor(message);
    }

}
