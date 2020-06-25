package eu.iteije.kitpvp.utils.game;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.SpawnSubCmd;
import eu.iteije.kitpvp.files.ConfigFile;
import eu.iteije.kitpvp.files.MapFile;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

        // Set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // Feed player
        player.setFoodLevel(20);

        // Add player to HashMap playersInGame
        playersInGame.put(player.getUniqueId(), mapName);
    }

    public static HashMap<UUID, Integer> countdown = new HashMap<>();
    private static HashMap<UUID, BukkitTask> countdownTask = new HashMap<>();

    public static void delayedLeave(Player player, boolean delay) {
        if (delay) {
            ConfigFile configFile = new ConfigFile(KitPvP.getInstance(), false);
            int delayInSeconds = configFile.get().getInt("game_leave_delay");
            // Check whether the number is valid
            if (delayInSeconds >= 0) {
                // Put delay in hashmap
                countdown.put(player.getUniqueId(), delayInSeconds);
                // Pre define message, so it don't have to be loaded over and over again
                String message = Message.get("game_leave_delay");

                // Create new BukkitTask and save it in a HashMap
                countdownTask.put(player.getUniqueId(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (countdown.get(player.getUniqueId()) > 0) {
                            // Get current countdown number and send it to the player
                            int currentCountdown = countdown.get(player.getUniqueId());
                            Message.sendToPlayer(player, Message.replace(message, "{seconds}", String.valueOf(countdown.get(player.getUniqueId()))), true);
                            // Subtract 1 from the HashMap countdown
                            countdown.put(player.getUniqueId(), (currentCountdown - 1));
                        } else {
                            // Remove countdown integer from hashmap
                            countdown.remove(player.getUniqueId());
                            // Cancel BukkitTask saved in countdownTask
                            BukkitTask bukkitTask = countdownTask.get(player.getUniqueId());
                            bukkitTask.cancel();
                            // Remove BukkitTask saved in hashmap
                            countdownTask.remove(player.getUniqueId());

                            // Call leave() method
                            leave(player);
                            // Success message
                            Message.sendToPlayer(player, Message.get("leave_success"), true);
                        }
                    }
                }.runTaskTimer(KitPvP.getInstance(), 0, 20));
            }
        } else {
            // Call leave() method
            leave(player);
            // Success message
            Message.sendToPlayer(player, Message.get("leave_success"), true);
        }
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
        SpawnSubCmd spawnSubCmd = new SpawnSubCmd(KitPvP.getInstance());
        try {
            spawnSubCmd.teleportToSpawn(player);
        } catch (IllegalArgumentException exception) {
            // If someone deleted the config file, or something else strange happened, it will throw a IllegalArgumentException
        }

        // Remove player from HashMap playersInGame
        playersInGame.remove(player.getUniqueId());
    }
}
