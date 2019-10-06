package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
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
    public MapFile mapFile;

    /**
     * @param player player in game
     * @param mapName map player is in
     * @param instance instance of KitPvP (main class)
     */
    public Game(Player player, String mapName, KitPvP instance) {
        this.player = player;
        this.mapName = mapName.toUpperCase();
        this.instance = instance;

        // Define mapFile
        mapFile = new MapFile(instance, false);
    }

    public void join() {
        // Send loading message, mapName is already uppercase
        String message = Message.get("interactsign_join_loading");
        message = Message.replace(message, "{map}", mapName);
        Message.sendToPlayer(player, message, true);

        // Teleport to random spawnpoint
        try {
            // Amount of spawnpoints
            int amountOfSpawnpoints = mapFile.get().getConfigurationSection("maps." + mapName + ".spawnpoints").getKeys(false).size();
            if (amountOfSpawnpoints == 0) {
                // No spawnpoints available message
                Message.sendToPlayer(player, Message.get("joingame_no_spawns"), true);
                return;
            } else {
                // Get random number between 1 and the value of amountOfSpawnspoints
                int randomNumber = (int) ((amountOfSpawnpoints) * Math.random()) + 1;

                // Receive location data from map file
                World world = Bukkit.getWorld(mapFile.get().getString("maps." + mapName + ".spawnpoints." + randomNumber + ".world"));
                double x = (Double) mapFile.get().get("maps." + mapName + ".spawnpoints." + randomNumber + ".x");
                double y = (Double) mapFile.get().get("maps." + mapName + ".spawnpoints." + randomNumber + ".y");
                double z = (Double) mapFile.get().get("maps." + mapName + ".spawnpoints." + randomNumber + ".z");
                Location spawnpoint = new Location(world, x, y, z);

                // Teleport player to location
                player.teleport(spawnpoint);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Save and clear player inventory
        savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();
        // TODO: Give kit

        // Set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // Add player to HashMap playersInGame
        playersInGame.put(player.getUniqueId(), mapName);
    }

    public void leave() {
        // Return inventory
        player.getInventory().clear();
        // Return old inventory contents
        player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
        // Delete saved inventory
        savedInventories.remove(player.getUniqueId());

        // Teleport back to spawn
        SpawnSubCmd spawnSubCmd = new SpawnSubCmd(instance);
        spawnSubCmd.teleportToSpawn(player);
        // Remove player from HashMap playersInGame
        playersInGame.remove(player.getUniqueId());
    }

    public static void leave(Player player) {
        // Return inventory
        player.getInventory().clear();
        // Return old inventory contents
        player.getInventory().setContents(savedInventories.get(player.getUniqueId()));
        // Delete saved inventory
        savedInventories.remove(player.getUniqueId());

        // Teleport back to spawn
        SpawnSubCmd spawnSubCmd = new SpawnSubCmd(KitPvP.getInstance());
        spawnSubCmd.teleportToSpawn(player);
        // Remove player from HashMap playersInGame
        playersInGame.remove(player.getUniqueId());
    }
}
