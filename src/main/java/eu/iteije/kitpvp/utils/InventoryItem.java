package eu.iteije.kitpvp.utils;

import eu.iteije.kitpvp.pluginutils.TransferMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryItem {


    /**
     * @param material material of the itemstack
     * @param amount amount of the material
     * @param displayName display name of the itemstack
     * @param lore lore of the itemstack
     * @return itemstack with all parameters applied
     */
    public static ItemStack createItem(Material material, int amount, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, amount); // initialize itemstack
        ItemMeta itemMeta = itemStack.getItemMeta(); // get current item meta of the ItemStack
        itemMeta.setDisplayName(TransferMessage.replaceColorCodes(displayName)); // change display name
        itemMeta.setLore(lore); // change item lore
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // hide attributes
        itemStack.setItemMeta(itemMeta); // assign new item meta

        return itemStack;
    }

    /**
     * @param material material of the itemstack
     * @param amount amount of the material
     * @param displayName display name of the itemstack
     * @return itemstack with all parameters applied
     */
    public static ItemStack createItem(Material material, int amount, String displayName) {
        ItemStack itemStack = new ItemStack(material, amount); // initialize itemstack
        ItemMeta itemMeta = itemStack.getItemMeta(); // get current item meta of the ItemStack
        itemMeta.setDisplayName(TransferMessage.replaceColorCodes(displayName)); // change display name
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // hide attributes
        itemStack.setItemMeta(itemMeta); // assign new item meta

        return itemStack;
    }

    /**
     * @param material material of the itemstack
     * @param amount amount of the material
     * @return itemstack with all parameters applied
     */
    public static ItemStack createItem(Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount); // initialize itemstack
        ItemMeta itemMeta = itemStack.getItemMeta(); // get current item meta of the ItemStack
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // hide attributes
        itemStack.setItemMeta(itemMeta); // assign new item meta

        return itemStack;
    }
}
