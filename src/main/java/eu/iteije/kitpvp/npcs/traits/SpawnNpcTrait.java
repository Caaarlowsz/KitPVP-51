package eu.iteije.kitpvp.npcs.traits;

import eu.iteije.kitpvp.KitPvP;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;

public class SpawnNpcTrait extends Trait {

    private final KitPvP instance;

    public SpawnNpcTrait(KitPvP instance, String name) {
        super(name);
        this.instance = instance;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        event.getClicker().sendMessage("Select kit and location");
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        event.getClicker().sendMessage("Select kit and teleport to center");
    }

}
