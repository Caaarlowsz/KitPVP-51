package eu.iteije.kitpvp.utils.editkits;

import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.editkits.inventories.KitsOverviewInventory;
import eu.iteije.kitpvp.utils.editkits.inventories.SelectKitActionInventory;
import org.bukkit.entity.Player;

public class OpenInventory {

    // KitsOverviewInventory - kitsoverview
    // SelectKitActionInventory - selectkitaction

    // Register inventory name and player
    private String inventoryName;
    private Player player;

    private String selectedKit;

    /**
     * @param inventoryName the inventory which has to be opened
     * @param player the player the inventory will be opened for
     */
    public OpenInventory(String inventoryName, Player player) {
        // Define inventory name and player variable
        this.inventoryName = inventoryName;
        this.player = player;
    }

    // Open a given inventory
    public void openInventory() {
        switch (inventoryName) {
            case "kitsoverview":
                KitsOverviewInventory kitsOverviewInventory = new KitsOverviewInventory(player);
                kitsOverviewInventory.open();
                break;
            case "selectkitaction":
                SelectKitActionInventory selectKitActionInventory = new SelectKitActionInventory(player, selectedKit);
                selectKitActionInventory.open();
                break;
            default:
                // Send error message
                Message.sendToPlayer(player, Message.get("editkits_openinventory_error"), true);
                break;
        }
    }

    public void setSelectedKit(String selectedKit) {
        this.selectedKit = selectedKit;
    }
}
