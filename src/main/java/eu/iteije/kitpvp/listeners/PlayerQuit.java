package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    // Instance variable of main class
    private KitPvP instance;
    private ConfigFile configFile;
    private MapFile mapFile;

    public PlayerQuit(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Player logging out
        Player player = event.getPlayer();

        // Return inventory to player if the player logs out
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            // Force stop setup (forced = false, because it isn't possible to send a message to a offline player)
            CreateMap.stopSetup(player, false);
        }
    }
}
