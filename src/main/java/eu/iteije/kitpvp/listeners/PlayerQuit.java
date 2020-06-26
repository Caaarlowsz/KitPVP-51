package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.data.UserCache;
import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.editkits.EditKits;
import eu.iteije.kitpvp.utils.game.Game;
import eu.iteije.kitpvp.utils.mapsetup.CreateMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;

public class PlayerQuit implements Listener {

    public PlayerQuit() {

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

        // Return inventory to player if the player logs out
        if (Game.savedInventories.containsKey(player.getUniqueId()) && Game.playersInGame.containsKey(player.getUniqueId())) {
            // Combat log thing
            if (player.getHealth() <= 10) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("kitpvp.notify.combatlog")) {
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");
                        Message.sendToPlayer(onlinePlayer, "&fCombat log notify: &e" + player.getName() + "&f logged out while at " +
                                decimalFormat.format(player.getHealth() / 2.0) + " health.", true);
                    }
                }
            }

            // Return inventory
            player.getInventory().clear();
            // Return old inventory contents
            player.getInventory().setContents(Game.savedInventories.get(player.getUniqueId()));
            // Delete saved inventory
            Game.savedInventories.remove(player.getUniqueId());
            // Remove the player from the 'players-ingame list'
            Game.playersInGame.remove(player.getUniqueId());
        }

        // If user is editing a kit, delete all saved data
        if (EditKits.newKits.containsKey(player.getUniqueId())) EditKits.newKits.remove(player.getUniqueId());
        if (EditKits.selectedKit.containsKey(player.getUniqueId())) EditKits.selectedKit.remove(player.getUniqueId());
        if (EditKits.isEditingName.containsKey(player.getUniqueId())) EditKits.isEditingName.remove(player.getUniqueId());
        if (EditKits.currentInventory.containsKey(player.getUniqueId())) EditKits.currentInventory.remove(player.getUniqueId());
        if (EditKits.isEditingIcon.containsKey(player.getUniqueId())) EditKits.isEditingIcon.remove(player.getUniqueId());

        // Remove user cache
        UserCache.removeUUID(player.getUniqueId());
    }
}
