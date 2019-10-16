package eu.iteije.kitpvp.utils.editkits;

import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.inventory.ItemStack;

public class NewKit {

    private String kitName;
    private ItemStack kitIcon;
    private ItemStack[] kitContent;

    public NewKit() {
        kitName = "&oniet ingesteld";

    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public ItemStack getKitIcon() {
        return kitIcon;
    }

    public String getKitIconAsString() {

        if (kitIcon == null) {
            return "&oniet ingesteld";
        } else {
            return Message.replace(kitIcon.getType().toString(), "_", " ").toLowerCase();
        }
    }

    public void setKitIcon(ItemStack kitIcon) {
        this.kitIcon = kitIcon;
    }

    public ItemStack[] getKitContent() {
        return kitContent;
    }

    public void setKitContent(ItemStack[] kitContent) {
        this.kitContent = kitContent;
    }


    public boolean isAllSet() {
        return getKitIcon() != null && getKitName() != null && getKitContent() != null && !getKitName().equals("&oniet ingesteld");
    }
}
