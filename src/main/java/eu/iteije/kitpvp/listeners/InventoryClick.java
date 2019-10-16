package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.Help;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.pluginutils.TransferMessage;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.editkits.NewKit;
import eu.iteije.kitpvp.utils.editkits.OpenInventory;
import eu.iteije.kitpvp.utils.editkits.inventories.EditKitInventory;
import eu.iteije.kitpvp.utils.editkits.inventories.KitsOverviewInventory;
import eu.iteije.kitpvp.utils.editkits.inventories.SelectKitActionInventory;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryClick implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    // Temporary
    public static HashMap<UUID, Boolean> forcedByPlayer = new HashMap<>();

    public InventoryClick(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Player who clicked in their inventory
        Player player = (Player) event.getWhoClicked();
        // Inventory the action is executed in
        Inventory inventory = event.getClickedInventory();

        // If a player is in a setup, the player is not able to interact with their inventory
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            // Close inventory, so players in creative can't double click
            player.closeInventory();
        }

        // If select menu inventory is opened, cancel event
        if (SelectKit.hasInventoryOpen.containsKey(player.getUniqueId())) {
            // Check whether the clicked inventory slot is a allowed kit slot
            if (Arrays.stream(SelectKit.allowedSlots).anyMatch(i -> i == event.getSlot())) {
                // If player is interacting with top inventory -> proceed
                if (inventory == event.getView().getTopInventory()) {
                    // Check if there is any kit in the slot
                    if (event.getCurrentItem().hasItemMeta()) {
                        // Cancel event, because a other class is triggered so the cancel event is unreachable from here (prevent NullPointerException)
                        event.setCancelled(true);
                        // Kit name
                        String kitName = TransferMessage.removeColors(event.getCurrentItem().getItemMeta().getDisplayName());
                        // Head back to SelectKit class to give the kit
                        SelectKit selectKit = new SelectKit(player, SelectKit.hasInventoryOpen.get(player.getUniqueId()));
                        selectKit.giveKit(player, kitName);
                    }
                }
            }

            // If clicked slot is 49 (close menu item) close selectkit inventory
            if (event.getSlot() == 49) {
                player.closeInventory();
            }
            event.setCancelled(true);
        }

        // Check whether a EditKit inventory is opened
        if (EditKits.currentInventory.containsKey(player.getUniqueId())) {
            // Cancel event here, because it could be possible the code never reaches this statement if placed at the end
            event.setCancelled(true);

            // Instance of EditKits
            EditKits editKits = new EditKits(player);

            if (inventory == event.getView().getBottomInventory()) {
                // Check if the player interacted with the EditKitContentInventory
                if (EditKits.currentInventory.get(player.getUniqueId()).equals("editkitcontent")) {
                    // Do not cancel the event
                    event.setCancelled(false);
                    return;
                }
            }

            // Check if the player is interacting with the top inventory
            if (inventory == event.getView().getTopInventory()) {

                // Check if the player interacted with the EditKitContentInventory
                if (EditKits.currentInventory.get(player.getUniqueId()).equals("editkitcontent")) {
                    // Check if the player interacted with item slot 44 or lower
                    if (event.getSlot() <= 44) {
                        event.setCancelled(false);
                        return;
                    }
                }

                // Inventory: KitsOverviewInventory
                if (EditKits.currentInventory.get(player.getUniqueId()).equals("kitsoverview")) {
                    // Check whether the clicked inventory slot is a allowed kit slot
                    if (Arrays.stream(SelectKit.allowedSlots).anyMatch(i -> i == event.getSlot())) {
                        // Check whether the clicked item is a existing kit or a available kit slot
                        if (!event.getCurrentItem().toString().equals(KitsOverviewInventory.undefinedKitItem.toString())) {
                            // The clicked item is considered a existing kit

                            // Kit name
                            String kitName = TransferMessage.removeColors(event.getCurrentItem().getItemMeta().getDisplayName());

                            // Save selected kit
                            EditKits.selectedKit.put(player.getUniqueId(), kitName);

                            // Open SelectKitActionInventory
                            OpenInventory openInventory = new OpenInventory("selectkitaction", player);
                            openInventory.openInventory();
                            return;
                        } else if (event.getCurrentItem().toString().equals(KitsOverviewInventory.undefinedKitItem.toString())) {
                            // The clicked item is considered a available kit slot

                            // Put player in newKits HashMap
                            EditKits.newKits.put(player.getUniqueId(), new NewKit());

                            // Open EditKitInventory
                            OpenInventory openInventory = new OpenInventory("editkit", player);
                            openInventory.openInventory();
                            return;
                        }
                    }
                }

                // Inventory: SelectKitActionInventory
                if (EditKits.currentInventory.get(player.getUniqueId()).equals("selectkitaction")) {
                    // Check whether the clicked block is a 'Edit kit' or 'Delete kit' block
                    if (Arrays.stream(SelectKitActionInventory.editKitSlots).anyMatch(i -> i == event.getSlot())) {
                        // Open edit kit menu
                        EditKitInventory editKitInventory = new EditKitInventory(player);
                        editKitInventory.open();
                        return;
                    }
                    if (Arrays.stream(SelectKitActionInventory.deleteKitSlots).anyMatch(i -> i == event.getSlot())) {
                        // Call delete kit method from EditKits
                        editKits.deleteKit();
                        // Close inventory
                        player.closeInventory();
                        // Send success message
                        String message = Message.get("editkits_deleted_success");
                        message = Message.replace(message, "{kit}", EditKits.selectedKit.get(player.getUniqueId()));
                        Message.sendToPlayer(player, message, true);
                        // Remove the player from the selectedKit HashMap
                        EditKits.selectedKit.remove(player.getUniqueId());
                        return;
                    }
                }

                // Inventory: EditKitInventory
                if (EditKits.currentInventory.get(player.getUniqueId()).equals("editkit")) {
                    // Check which slot is clicked
                    // Slot 20 is the name tag (change name)
                    if (event.getSlot() == 20) {
                        // Add player to isEditingName HashMap
                        EditKits.isEditingName.put(player.getUniqueId(), true);
                        // Close inventory
                        player.closeInventory();

                        // Send explanation message
                        List<String> explanation = Arrays.asList(
                                "&fTyp de nieuwe naam van de kit.",
                                "&fAls je wil stoppen typ dan &cannuleren&f."
                        );
                        List<String> empty = Arrays.asList(
                                "",
                                ""
                        );
                        Help help = new Help(explanation, empty);
                        help.send(player);
                        return;
                    }
                    // Slot 22 is the item frame (change icon)
                    if (event.getSlot() == 22) {
                        // Add player to isEditingIcon HashMap
                        EditKits.isEditingIcon.put(player.getUniqueId(), true);
                        // Close inventory
                        player.closeInventory();

                        // Send explation message
                        List<String> explanation = Arrays.asList(
                                "&fHet eerstvolgende blok waarmee je klikt, wordt het nieuwe icoon.",
                                "&fAls je wil stoppen typ dan &cannuleren&f."
                        );
                        List<String> empty = Arrays.asList(
                                "",
                                ""
                        );
                        Help help = new Help(explanation, empty);
                        help.send(player);
                        return;
                    }
                    // Slot 24 is the chest (change kit contents)
                    if (event.getSlot() == 24) {
                        OpenInventory openInventory = new OpenInventory("editkitcontent", player);
                        openInventory.openInventory();
                    }

                    // Check if player is setting up a new kit
                    if (EditKits.newKits.containsKey(player.getUniqueId())) {
                        // Check if player is interacting with the 'finish item' slot (slot 53)
                        if (event.getSlot() == 53) {
                            // NewKit (of player)
                            NewKit newKit = EditKits.newKits.get(player.getUniqueId());
                            // Check if all required data is set
                            if (newKit.isAllSet()) {
                                // KitFile instance
                                KitFile kitFile = new KitFile(instance, false);

                                // Kit name
                                String kitName = newKit.getKitName();

                                // Save icon
                                kitFile.get().set("kits." + kitName + ".icon", newKit.getKitIcon().getType().toString());
                                // Save content
                                ItemStack[] items = newKit.getKitContent();
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
                                String message = Message.get("editkits_newkit_success");
                                message = Message.replace(message, "{kitname}", kitName);
                                Message.sendToPlayer(player, message, true);

                                // Open KitOverviewInventory
                                OpenInventory openInventory = new OpenInventory("kitsoverview", player);
                                openInventory.openInventory();
                            } else {
                                // Failed message
                                Message.sendToPlayer(player, Message.get("editkits_newkit_failed"), true);
                            }
                        }
                    }
                }

                // In every single inventory the 'Close menu' button is at item slot 49, so if that slot is clicked, close menu
                if (event.getSlot() == 49) {
                    player.closeInventory();
                }
                // In all inventories, except for the KitsOverviewInventory, a 'Return' item is added at slot 45
                if (event.getCurrentItem() != null && event.getSlot() == 45) {
                    // If the player hits the return button, the selectedKit don't have to be deleted
                    if (EditKits.currentInventory.get(player.getUniqueId()).equals("editkitcontent")) {
                        // Only proceed if the player actually selected a kit
                        if (EditKits.selectedKit.containsKey(player.getUniqueId())) {
                            // Put the player in the HashMap
                            forcedByPlayer.put(player.getUniqueId(), true);
                        }

                    }
                    // Save current inventory name
                    String currentInventory = EditKits.currentInventory.get(player.getUniqueId());
                    // Execute separate actions for all inventories
                    switch (currentInventory) {
                        // If the player is in the SelectKitActionInventory or EditKitInventory, it will return to the KitsOverViewInventory
                        case "selectkitaction": case "editkit":
                            KitsOverviewInventory kitsOverviewInventory = new KitsOverviewInventory(player);
                            kitsOverviewInventory.open();
                            break;
                        case "editkitcontent":
                            EditKitInventory editKitInventory = new EditKitInventory(player);
                            editKitInventory.open();
                            break;
                    }
                }
            }
        }
    }
}