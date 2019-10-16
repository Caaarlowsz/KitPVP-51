package eu.iteije.kitpvp.utils.editkits;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.KitFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    // The kit a player (admin) selected
    public static HashMap<UUID, String> selectedKit = new HashMap<>();

    // HashMap of players editing a kit name/editing kit icon
    public static HashMap<UUID, Boolean> isEditingName = new HashMap<>();
    public static HashMap<UUID, Boolean> isEditingIcon = new HashMap<>();

    // Kits in progress
    public static HashMap<UUID, NewKit> newKits = new HashMap<>();

    /**
     * @param inventoryName inventory which has to be opened
     */
    public void openInventory(String inventoryName) {
        OpenInventory openInventory = new OpenInventory(inventoryName, player);
        openInventory.openInventory();
    }


    // Delete a kit
    public void deleteKit() {
        // Save kit name
        String kitName = selectedKit.get(player.getUniqueId());

        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Delete the kit
        kitFile.get().set("kits." + kitName, null);
        kitFile.save();
    }


    // Edit the name of a kit
    public void editKitName(String newKitName) {
        // KitFile instance
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        if (newKits.containsKey(player.getUniqueId())) {
            // Check whether the new kit name is in use, 'exists' is false by default, if there is no kit in the file, it won't interrupt the create kit process
            Boolean exists = false;
            for (String existingKit : kitFile.get().getConfigurationSection("kits").getKeys(false)) {
                // Set the boolean to true if the kit name is taken
                exists = existingKit.toLowerCase().equalsIgnoreCase(newKitName.toLowerCase());
                // If the boolean is true, break out of loop
                if (exists) break;
            }

            // If the name does not exists, proceed
            if (!exists) {
                // Set kit name and assign it to the NewKit object
                NewKit newKit = newKits.get(player.getUniqueId());
                newKit.setKitName(newKitName);
                newKits.put(player.getUniqueId(), newKit);

                // Send success message
                String message = Message.get("editkits_newkit_changename_success");
                message = Message.replace(message, "{kitname}", newKitName);
                Message.sendToPlayer(player, message, true);
            } else {
                // Send exists message
                Message.sendToPlayer(player, Message.get("editkits_newkit_changename_exists"), true);
            }

        } else {
            // Current name of the kit
            String currentKitName = selectedKit.get(player.getUniqueId());

            // Check if all characters in the string are alphanumeric
            if (newKitName.matches("[a-zA-Z]+")) {
                // Check whether the new kit name is in use, 'exists' is false by default, if there is no kit in the file, it won't interrupt the create kit process
                Boolean exists = false;
                for (String existingKit : kitFile.get().getConfigurationSection("kits").getKeys(false)) {
                    // Set the boolean to true if the kit name is taken
                    exists = existingKit.toLowerCase().equalsIgnoreCase(newKitName.toLowerCase());
                    // If the boolean is true, break out of loop
                    if (exists) break;
                }

                if (!exists) {
                    // Copy all data and place it under the new name
                    for (String current : kitFile.get().getConfigurationSection("kits." + currentKitName).getKeys(true)) {
                        String value = kitFile.get().getString("kits." + currentKitName + "." + current);
                        kitFile.get().set("kits." + newKitName + "." + current, value);
                    }
                    // Remove old kit data
                    kitFile.get().set("kits." + currentKitName, null);
                    // Save KitFile
                    kitFile.save();

                    // Send success message
                    String message = Message.get("editkits_changename_success");
                    message = Message.replace(message, "{oldname}", currentKitName);
                    message = Message.replace(message, "{newname}", newKitName);
                    Message.sendToPlayer(player, message, true);

                    // Set the new name as selectedKit
                    selectedKit.put(player.getUniqueId(), newKitName);
                } else {
                    // Kit name exists error
                    Message.sendToPlayer(player, Message.get("editkits_changename_exists"), true);
                }
            } else {
                // Only alphanumeric characters message
                Message.sendToPlayer(player, Message.get("editkits_changename_alphanumeric_only"), true);
            }
        }

    }

    public void editKitIcon(ItemStack newIcon) {
        // Instance of KitFile
        KitFile kitFile = new KitFile(KitPvP.getInstance(), false);

        // Check if the player is editing a new kit or a existing kit
        if (newKits.containsKey(player.getUniqueId())) {
            // Set kit name and assign it to the NewKit object
            NewKit newKit = newKits.get(player.getUniqueId());

            Material oldIcon;
            try {
                oldIcon = newKit.getKitIcon().getType();
            } catch (Exception exception) {
                oldIcon = Material.AIR;
            }

            if (newIcon.getType() == oldIcon) {
                // Same material error
                Message.sendToPlayer(player, Message.get("editkits_newkit_changeicon_same"), true);
                return;
            }

            newKit.setKitIcon(newIcon);
            newKits.put(player.getUniqueId(), newKit);

            // Send success message
            String message = Message.get("editkits_newkit_changeicon_success");
            message = Message.replace(message, "{icon}", Message.replace(newIcon.getType().toString(), "_", " ").toLowerCase());
            Message.sendToPlayer(player, message, true);
        } else {
            // Save current kit
            String currentKit = selectedKit.get(player.getUniqueId());
            // Save current icon
            Material currentIcon = Material.getMaterial(kitFile.get().getString("kits." + currentKit + ".icon"));

            // If the new icon is different from the old one, change it
            if (newIcon.getType() != currentIcon) {
                kitFile.get().set("kits." + currentKit + ".icon", newIcon.getType().toString());
                kitFile.save();

                // Send success message
                String message = Message.get("editkits_changeicon_success");
                message = Message.replace(message, "{oldicon}", Message.replace(currentIcon.toString(), "_", " ").toLowerCase());
                message = Message.replace(message, "{newicon}", Message.replace(newIcon.getType().toString(), "_", " ").toLowerCase());
                Message.sendToPlayer(player, message, true);
            } else {
                // Same material error
                Message.sendToPlayer(player, Message.get("editkits_changeicon_same"), true);
            }
        }
    }

}
