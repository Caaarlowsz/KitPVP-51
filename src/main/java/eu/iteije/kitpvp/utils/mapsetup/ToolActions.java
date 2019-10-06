package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ToolActions {

    // Main class instance
    private static KitPvP instance = KitPvP.getInstance();

    // Undefined intsance of MapFile
    private static MapFile mapFile;

    // Instance of SpawnPlate
    private static SpawnPlate spawnPlate = new SpawnPlate(instance);

    // Exit tool
    public static void useExitTool(Player player) {
        // Return inventory and remove light weighted pressure plates
        CreateMap.stopSetup(player, false);
        // Send success message
        Message.sendToPlayer(player, Message.get("createmap_exit_success"), true);
    }

    // Spawnpoint tool
    public static void useSpawnpointTool(Player player, Block block) {
        // Add new plate location
        spawnPlate.addNewLocation(player, block);
        // Send success message
        Message.sendToPlayer(player, Message.get("createmap_spawnpoint_success"), true);
    }

    // Finish tool
    public static void useFinishTool(Player player) {
        // Save data to maps.yml
        // Remove light weighted pressure plates
        // Send a number of spawnpoints if setup is finished an successful
    }


    // Get spawn plate
    public static SpawnPlate getSpawnPlate() {
        return spawnPlate;
    }
}
