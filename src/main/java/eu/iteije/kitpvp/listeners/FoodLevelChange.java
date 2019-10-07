package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public FoodLevelChange(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // Check whether this event happened to a player
        if (event.getEntity() instanceof Player) {
            // Player the food level changed on
            Player player = (Player) event.getEntity();

            if (Game.playersInGame.containsKey(player.getUniqueId())) {
                // Instance of ConfigFile
                ConfigFile configFile = new ConfigFile(instance, false);
                // Look for food level change boolean in config
                try {
                    boolean foodLevelsChange = configFile.get().getBoolean("game_hunger");
                    if (!foodLevelsChange) {
                        event.setCancelled(true);
                    }
                } catch (NullPointerException exception) {
                    // Leave it empty, its just a handler for a NullPointException
                }
            } else {
                // Hunger is disabled by default
                event.setCancelled(true);
            }
        }


    }
}
