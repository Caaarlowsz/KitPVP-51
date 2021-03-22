package eu.iteije.kitpvp.npcs.listeners;

import eu.iteije.kitpvp.KitPvP;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    private final KitPvP instance;

    public NPCClickListener(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        String map = event.getNPC().data().get("map");
        event.getClicker().sendMessage("Select kit and location (" + map + ")");

    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        String map = event.getNPC().data().get("map");
        event.getClicker().sendMessage("Select kit and teleport to center (" + map + ")");
    }

}
