package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.NewKit;
import eu.iteije.kitpvp.utils.editkits.inventories.EditKitContentInventory;
import eu.iteije.kitpvp.utils.game.SelectKit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryClose implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public InventoryClose(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Player who close a inventory
        Player player = (Player) event.getPlayer();

        // Check whether the opened inventory is the select kit inventory
        // If so, remove the player from the HashMap
        if (SelectKit.hasInventoryOpen.containsKey(player.getUniqueId())) {
            // Remove player from HashMap
            SelectKit.hasInventoryOpen.remove(player.getUniqueId());
            return;
        }

        // Remove player from EditKits.currentInventory if it was in a EditKit inventory
        if (EditKits.currentInventory.containsKey(player.getUniqueId())) {
            // Check if the exited inventory is the EditKitContentInventory
            if (EditKits.currentInventory.get(player.getUniqueId()).equals("editkitcontent")) {
                // Contents of top inventory
                List<ItemStack> itemList = new ArrayList<>();
                for (int i = 0; i < 44; i++) {
                    if (event.getView().getTopInventory().getItem(i) != null) {
                        itemList.add(event.getView().getTopInventory().getItem(i));
                    }
                }
                ItemStack[] items = itemList.toArray(new ItemStack[0]);

                if (items.length == 0) items = null;

                // Check if the player is creating a new kit or editing a existing kit
                if (EditKits.newKits.containsKey(player.getUniqueId())) {
                    // Edit new kit
                    NewKit newKit = EditKits.newKits.get(player.getUniqueId());
                    newKit.setKitContent(items);

                    // Push new content to the NewKit object
                    EditKits.newKits.put(player.getUniqueId(), newKit);
                } else {
                    // Edit existing kit

                    // KitFile instance
                    KitFile kitFile = new KitFile(instance, false);

                    // Kit name
                    String kitName = EditKits.selectedKit.get(player.getUniqueId());

                    // Check if items is not null
                    if (items != null) {
                        // Check if there is any difference between the old items and this ones
                        if (!Arrays.equals(EditKitContentInventory.currentItems.get(player.getUniqueId()), items)) {
                            // Delete old content
                            kitFile.get().set("kits." + kitName + ".items", null);
                            kitFile.get().set("kits." + kitName + ".gear", null);

                            // Save content
                            for (ItemStack item : items) {
                                String material = item.getType().toString();
                                int amount = item.getAmount();

                                if (material.contains("HELMET")) {
                                    kitFile.get().set("kits." + kitName + ".gear.HELMET", material);
                                } else if (material.contains("CHESTPLATE")) {
                                    kitFile.get().set("kits." + kitName + ".gear.CHESTPLATE", material);
                                } else if (material.contains("LEGGINGS")) {
                                    kitFile.get().set("kits." + kitName + ".gear.LEGGINGS", material);
                                } else if (material.contains("BOOTS")) {
                                    kitFile.get().set("kits." + kitName + ".gear.BOOTS", material);
                                } else {
                                    kitFile.get().set("kits." + kitName + ".items." + material + ".amount", amount);
                                }
                            }

                            // Save file
                            kitFile.save();

                            // Send success message
                            String message = Message.get("editkits_changeitems_success");
                            message = Message.replace(message, "{kitname}", kitName);
                            Message.sendToPlayer(player, message, true);

                            // If the player clicked escape or whatever, the player has to be removed from any EditKits hashmap
                            if (InventoryClick.forcedByPlayer.containsKey(player.getUniqueId())) {
                                // Remove player from HashMap
                                InventoryClick.forcedByPlayer.remove(player.getUniqueId());
                            } else {
                                // Remove player from selectedKit HashMap
                                EditKits.selectedKit.remove(player.getUniqueId());
                            }
                        }
                    } else {
                        // Send error message
                        Message.sendToPlayer(player, Message.get("editkits_changeitems_empty"), true);
                        // If the player clicked escape or whatever, the player has to be removed from any EditKits hashmap
                        if (InventoryClick.forcedByPlayer.containsKey(player.getUniqueId())) {
                            // Remove player from HashMap
                            InventoryClick.forcedByPlayer.remove(player.getUniqueId());
                        } else {
                            // Remove player from selectedKit HashMap
                            EditKits.selectedKit.remove(player.getUniqueId());
                        }
                    }
                }
            }
            // Remove player from HashMap
            EditKits.currentInventory.remove(player.getUniqueId());
        }
    }
}
