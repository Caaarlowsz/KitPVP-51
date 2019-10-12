package eu.iteije.kitpvp.utils.editkits;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class EditKits {

    // Undefined player variable
    Player player;

    /**
     * @param player player entering the menu
     */
    public EditKits(Player player) {
        this.player = player;
    }

    // In this HashMap the current 'editkit' inventory is saved
    public static HashMap<UUID, String> currentInventory = new HashMap<>();

    /**
     * @param inventoryName inventory which has to be opened
     */
    public void openInventory(String inventoryName) {
        OpenInventory openInventory = new OpenInventory(inventoryName, player);
        openInventory.openInventory();
    }

}
