package eu.iteije.kitpvp.utils.editkits.inventories;

import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.entity.Player;

public class SelectKitActionInventory {

    // Inventory title
    public static String EDIT_KIT_SELECT_KIT_ACTION_TITLE = TransferMessage.replaceColorCodes("&c&lSelecteer een actie");

    // Undefined player variable
    private Player player;

    /**
     * @param player player the inventory will be opened for
     */
    public SelectKitActionInventory(Player player, String kitName) {
        this.player = player;
    }

    public void open() {

    }
}
