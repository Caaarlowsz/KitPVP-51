package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import eu.iteije.kitpvp.utils.mapsetup.SpawnPlate;
import eu.iteije.kitpvp.utils.mapsetup.ToolActions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ConcurrentModificationException;
import java.util.List;

public class BlockBreak implements Listener {

    // Instance variable of main class
    private KitPvP instance;

    public BlockBreak(KitPvP instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Player who broke the block
        Player player = event.getPlayer();
        // Broken block
        Block block = event.getBlock();

        // Make sure player is in setup, before getting the instance of SpawnPlate from ToolActions (to prevent NullPointerException)
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            // Instance of SpawnPlate from ToolActions
            SpawnPlate spawnPlate = ToolActions.getSpawnPlate();
            // Location of broken block
            Location blockLocation = block.getLocation();

            // Instantly allow the player in setup to break not-solid blocks, like grass, flowers, snow, seeds
            if (!block.getType().isSolid()) {
                event.setCancelled(false);
                return;
            }

            try {
                // All locations of placed spawn plates
                List<Location> locations = ToolActions.getSpawnPlate().getLocations().get(player.getUniqueId());
                event.setCancelled(true);
                // Loop through all locations
                for (Location location : locations) {
                    // If block location is the same as a saved plate location, destroy it
                    if (compareLocation(blockLocation, location)) {
                        spawnPlate.destroyPlate(player, blockLocation, true);
                        // Success message
                        Message.sendToPlayer(player, Message.get("createmap_spawnpoint_deleted"), true);
                        event.setCancelled(false);
                        // Get the remaining locations, if there aren't plates left, player is removed from 'locations' Map to prevent a NullPointerException
                        locations = ToolActions.getSpawnPlate().getLocations().get(player.getUniqueId());
                        if (locations.isEmpty()) {
                            // Remove player from Map
                            spawnPlate.removeLocations(player);
                        }
                    } else {
                        // If block location is not equal to saved location, cancel event
                        event.setCancelled(true);
                    }
                }
            } catch (NullPointerException | ConcurrentModificationException exception) {
                // Leave it empty, it's just a handler
            }
        } else {
            // Location of broken block
            Location blockLocation = block.getLocation();
            // Loop through online players
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                try {
                    // Check whether the plate is a spawn plate of someone else
                    List<Location> locations = ToolActions.getSpawnPlate().getLocations().get(onlinePlayer.getUniqueId());
                    // Loop through locations
                    for (Location location : locations) {
                        // Plate have to be indestructible
                        if (compareLocation(blockLocation, location)) {
                            event.setCancelled(true);
                            return;
                        }
                        // The block below a plate have to be invulnerable
                        if (compareLocation(blockLocation, location.add(0, -1, 0))) {
                            event.setCancelled(true);
                            return;
                        }
                        // Add y + 1 to location, because it is subtracted before
                        location.add(0, 1, 0);
                    }
                } catch (NullPointerException exception) {
                    // Leave it empty, it's just a handler (enable if there is suspicious behavior around this class)
                }
            }
        }
    }

    private boolean compareLocation(Location location1, Location location2) {
        // Compare world, x, y, z, pitch and yaw with each other, if they're all the same, return true
        if (location1.getWorld() == location2.getWorld() &&
                location1.getX() == location2.getX() &&
                location1.getY() == location2.getY() &&
                location1.getZ() == location2.getZ() &&
                location1.getPitch() == location2.getPitch() &&
                location1.getYaw() == location2.getYaw()) return true;
        return false;
    }

}
