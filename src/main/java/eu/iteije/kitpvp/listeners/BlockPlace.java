package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    public BlockPlace() {

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        // Define player executing this event
        Player player = event.getPlayer();
        // Block placed
        Block block = event.getBlockPlaced();

        // Check whether the player is in setup or not
        if (CreateMap.savedInventories.containsKey(player.getUniqueId())) {
            // Double check used block is a setup block, if so, proceed
            String setupItem = CreateMap.checkSetupItem(block.getType(), player);
            if (!setupItem.equals("none")) {
                event.setCancelled(true);
                // Continue
            }
        }
    }
}
