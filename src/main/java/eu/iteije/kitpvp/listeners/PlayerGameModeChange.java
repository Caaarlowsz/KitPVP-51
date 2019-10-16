package eu.iteije.kitpvp.listeners;

import eu.iteije.kitpvp.pluginutils.Message;
import eu.iteije.kitpvp.utils.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChange implements Listener {

    public PlayerGameModeChange() {

    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        // Player who changes it's gamemode
        Player player = event.getPlayer();
        // If player is in game, it shouldn't be able to change it's gamemode
        if (Game.playersInGame.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            Message.sendToPlayer(player, Message.get("game_gamemode_change"), true);
        }
    }

}
