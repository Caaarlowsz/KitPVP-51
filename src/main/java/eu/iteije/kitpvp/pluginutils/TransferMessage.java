package eu.iteije.kitpvp.pluginutils;

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

}
