package eu.iteije.kitpvp.npcs.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.memory.GameLocations;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.game.SelectKit;
import eu.iteije.kitpvp.utils.game.SelectSpawn;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
        Player clicker = event.getClicker();

        if (!Game.playersInGame.containsKey(clicker.getUniqueId())) {
            SelectSpawn selectSpawn = new SelectSpawn(map, instance.getGameLocations().get(map), clicker);
            selectSpawn.openMenu();
        }
    }


    // Center teleport
    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        String map = event.getNPC().data().get("map");
        Player clicker = event.getClicker();

        if (!Game.playersInGame.containsKey(clicker.getUniqueId())) {
            Location center = instance.getGameLocations().get(map).getCenter();
            if (center != null) {
                GameLocations.select(clicker.getUniqueId(), center);

                SelectKit select = new SelectKit(event.getClicker(), map);
                select.openMenu();
            } else {
                Message.sendToPlayer(clicker, "&cNo center location found for this map!", false);
            }
        }
    }

}
