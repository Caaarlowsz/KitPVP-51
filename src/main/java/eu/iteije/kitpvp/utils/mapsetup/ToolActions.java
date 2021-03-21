package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class ToolActions {

    // Main class instance
    private static KitPvP instance = KitPvP.getInstance();

    // Undefined intsance of MapFile
    private static PluginFile mapFile;

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
        if (spawnPlate.addNewLocation(player, block)) {
            // Send success message
            Message.sendToPlayer(player, Message.get("createmap_spawnpoint_success"), true);
        }
    }

    // Finish tool
    public static void useFinishTool(Player player) {
        // Define instance of MapFile
        mapFile = KitPvP.getInstance().getMapFile();

        // Saving map name has to be placed higher than stopSetup(), because the player is removed from the mapNames HashMap there
        String mapName = CreateMap.getMapName(player);

        // If there are no spawn points set, the map won't be saved
        // Try statement to prevent NullPointerException
        try {
            // Amount of spawn plates
            int spawnPlates = spawnPlate.getLocations().get(player.getUniqueId()).size();

            // Save data to maps.yml
            // Get list of locations
            List<Location> locations = ToolActions.getSpawnPlate().getLocations().get(player.getUniqueId());
            int count = 1;
            // Loop through locations
            for (Location location : locations) {
                mapFile.get().set("maps." + mapName.toUpperCase() + ".spawnpoints." + count + ".world", location.getWorld().getName());
                mapFile.get().set("maps." + mapName.toUpperCase() + ".spawnpoints." + count + ".x", location.getX());
                mapFile.get().set("maps." + mapName.toUpperCase() + ".spawnpoints." + count + ".y", location.getY());
                mapFile.get().set("maps." + mapName.toUpperCase() + ".spawnpoints." + count + ".z", location.getZ());
                mapFile.save();
                count++;
            }

            // Remove light weighted pressure plates (gold plates)
            CreateMap.stopSetup(player, false);

            // Send a number of spawnpoints if setup is finished an successful
            String message = Message.get("createmap_success");
            message = Message.replace(message, "{map}", mapName.toUpperCase());
            message = Message.replace(message, "{spawnpoints}", String.valueOf(spawnPlates));
            Message.sendToPlayer(player, message, true);
        } catch (NullPointerException exception) {
            // Stop the setup anyway
            CreateMap.stopSetup(player, false);
        }
    }


    // Get spawn plate
    public static SpawnPlate getSpawnPlate() {
        return spawnPlate;
    }
}
