package eu.iteije.kitpvp.memory;

import eu.iteije.kitpvp.files.PluginFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameLocations {

    @Getter @Setter
    private Location center;
    @Getter
    private Map<String, Location> spawnPoints;

    private static Map<UUID, Location> selectedLocations = new HashMap<>();

    public GameLocations(String mapName, PluginFile file) {
        this.center = getLocation(file.get().getConfigurationSection("maps." + mapName + ".center"));
        this.spawnPoints = new HashMap<>();

        ConfigurationSection spawnPoints = file.get().getConfigurationSection("maps." + mapName + ".spawnpoints");
        if (spawnPoints != null) {
            for (String key : spawnPoints.getKeys(false)) {
                Location location = getLocation(spawnPoints.getConfigurationSection(key));
                if (location != null) this.spawnPoints.put(key, location);
            }
        }
    }

    private Location getLocation(ConfigurationSection data) {
        if (data == null) return null;

        World world = Bukkit.getWorld(data.getString("world"));
        double x = data.getDouble("x");
        double y = data.getDouble("y");
        double z = data.getDouble("z");
        float yaw = -90;
        float pitch = 0;

        try {
            yaw = Float.parseFloat(String.valueOf(data.getString("yaw")));
            pitch = Float.parseFloat(String.valueOf(data.getString("pitch")));
        } catch (NumberFormatException | NullPointerException ignored) {}

        return new Location(world, x, y, z, yaw, pitch);
    }


    public static void select(UUID uuid, Location location) {
        selectedLocations.put(uuid, location);
    }

    public static Location getSelectedLocation(UUID uuid) {
        return selectedLocations.get(uuid);
    }

}

