package eu.iteije.kitpvp.utils.mapsetup;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class SpawnPlate {

    // Main class instance
    private KitPvP instance;

    // All new plates (per player) will be saved here
    private Map<UUID, List<Location>> locations = new HashMap<>();

    /**
     * @param instance instance of main class (KitPvP)
     */
    public SpawnPlate(KitPvP instance) {
        this.instance = instance;
    }

    /**
     * @param player player who placed the block
     * @param block block the plate location is made up of
     * @return whether a new plate is set or not
     */
    public boolean addNewLocation(Player player, Block block) {
        if (!block.getType().toString().contains("PRESSURE_PLATE")) {
            // Check whether the block is solid or not
            if (block.getType().isSolid()) {
                // Make up a new location out of block data, increment Y by 1 block, where the plate will be placed on
                Location plateLocation = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ());
                // Spawn block at location
                plateLocation.getBlock().setType(Material.GOLD_PLATE);
                // Add new plate to locations Map
                List<Location> locationList = locations.get(player.getUniqueId());

                // If list does not exists, create it
                if (locationList == null) {
                    locationList = new ArrayList<>();
                    locationList.add(plateLocation);
                    locations.put(player.getUniqueId(), locationList);
                } else {
                    // Add item if it is not already in the list
                    if (!locationList.contains(plateLocation)) locationList.add(plateLocation);
                    locations.remove(player.getUniqueId());
                    locations.put(player.getUniqueId(), locationList);
                }
                return true;
            }
        }
        return false;
    }

    // Get Map of locations (key: uuid)
    public Map<UUID, List<Location>> getLocations() {
        return locations;
    }

    // Destroy a plate and remove it (if instantRemove = true) from locations variable
    // instantRemove boolean is added to prevent ConcurrentModificationExceptions
    public void destroyPlate(Player player, Location location, boolean instantRemove) {
        // Set the block to type AIR
        location.getBlock().setType(Material.AIR);
        // If instantRemove = true, remove plate location from Map
        if (instantRemove) {
            locations.get(player.getUniqueId()).remove(location);
        }
    }

    // Remove all location data from a certain player
    public void removeLocations(Player player) {
        locations.remove(player.getUniqueId());
    }
}
