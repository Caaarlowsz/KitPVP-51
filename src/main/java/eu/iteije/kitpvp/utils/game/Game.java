package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.subcommands.SpawnSubCmd;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.memory.GameLocations;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class Game {

    // Parameters set by constructor
    private KitPvP instance;
    private Player player;
    private String mapName;

    // Public variables
    public static HashMap<UUID, String> playersInGame = new HashMap<>();
    public static HashMap<UUID, ItemStack[]> savedInventories = new HashMap<>();

    // File instances
    public PluginFile mapFile;

    /**
     * @param player   player in game
     * @param mapName  map player is in
     * @param instance instance of KitPvP (main class)
     */
    public Game(Player player, String mapName, KitPvP instance) {
        this.player = player;
        this.mapName = mapName.toUpperCase();
        this.instance = instance;

        // Define mapFile
        this.mapFile = KitPvP.getInstance().getMapFile();
    }

    public void join() {
        // Send teleport message
        Message.sendToPlayer(player, "&aJoining game...", false);

        // Teleport to previously set spawnpoint
        Location spawn = GameLocations.getSelectedLocation(player.getUniqueId());
        if (spawn == null) {
            Message.sendToPlayer(player, "&cSpawn location is invalid!", false);
            return;
        }

        player.teleport(spawn);

        // Set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // Feed player
        player.setFoodLevel(20);

        // Add player to HashMap playersInGame
        playersInGame.put(player.getUniqueId(), mapName);
    }

    public static void leave(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect clear " + player.getName());
        // Return inventory
        player.getInventory().clear();
        // Return old inventory contents
        player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
        // Delete saved inventory
        savedInventories.remove(player.getUniqueId());

        // Teleport back to spawn
        SpawnSubCmd spawnSubCmd = new SpawnSubCmd();
        try {
            spawnSubCmd.teleportToSpawn(player);
        } catch (IllegalArgumentException exception) {
            // If someone deleted the config file, or something else strange happened, it will throw a IllegalArgumentException
        }

        // Remove player from HashMap playersInGame
        playersInGame.remove(player.getUniqueId());
    }
}
