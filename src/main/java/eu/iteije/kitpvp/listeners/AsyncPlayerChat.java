package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.OpenInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    public AsyncPlayerChat() {

    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // Player who chatted
        Player player = event.getPlayer();
        // Message the player sent
        String message = event.getMessage();

        // Check whether the player is editing a kit name
        if (EditKits.isEditingName.containsKey(player.getUniqueId())) {
            // Cancel event
            event.setCancelled(true);

            // If the player typed 'annuleren', then cancel and return to the EditKitInventory
            if (message.equalsIgnoreCase("annuleren")) {
                // Send cancelled message
                Message.sendToPlayer(player, Message.get("editkits_changename_cancelled"), true);
            } else {
                // Check if the new kit name is not longer than 40 chars
                if (message.length() <= 40) {
                    // Call editKitName method from EditKits
                    EditKits editKits = new EditKits(player);
                    editKits.editKitName(message);
                } else {
                    // Send 'kitname too long' message
                    Message.sendToPlayer(player, Message.get("editkits_changename_too_long"), true);
                }
            }

            // Remove player from isEditingName HashMap
            EditKits.isEditingName.remove(player.getUniqueId());

            // Open EditKitInventory
            OpenInventory openInventory = new OpenInventory("editkit", player);
            openInventory.openInventory();
        }

        // Check if the player is editing a kit icon
        if (EditKits.isEditingIcon.containsKey(player.getUniqueId())) {
            // If the player typed 'annuleren', then cancel and return to the EditKitInventory
            if (message.equalsIgnoreCase("annuleren")) {
                // Cancel event
                event.setCancelled(true);

                // Send cancelled message
                Message.sendToPlayer(player, Message.get("editkits_changeicon_cancelled"), true);

                // Remove player from isEditingIcon HashMap
                EditKits.isEditingIcon.remove(player.getUniqueId());

                // Open EditKitInventory
                OpenInventory openInventory = new OpenInventory("editkit", player);
                openInventory.openInventory();
            }
        }
    }
}
