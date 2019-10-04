package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.entity.Player;

public class ToolActions {

    // Main class instance
    private KitPvP instance = KitPvP.getInstance();

    // Since it have to be possible to add easily add other tool actions, no constructor here

    // Exit tool
    public static void useExitTool(Player player) {
        // Remove all existing light weighted (gold) pressure plates placed in current setup
        // Return inventory
    }

    // Spawnpoint tool
    public static void useSpawnpointTool(Player player) {
        // Temporary save location in maps.yml
        // Cancel blockplace event, place light weighted plate
    }

    // Finish tool
    public static void useFinishTool(Player player) {
        // Save data to maps.yml
        // Remove light weighted pressure plates
        // Send a number of spawnpoints if setup is finished an successful
    }

}
