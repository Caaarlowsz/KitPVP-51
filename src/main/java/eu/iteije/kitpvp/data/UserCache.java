package eu.iteije.kitpvp.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UserCache {

    public static HashMap<UUID, Integer> kills = new HashMap<>();
    public static HashMap<UUID, Integer> deaths = new HashMap<>();

    public static Map<String, Integer> leaderboard = new LinkedHashMap<>();

    // Update cache
    public static void updateKills(UUID uuid, int increment) {
        // If kills HashMap contains the uuid, getKills returns a Integer, otherwise getKills would return null, which would cause a error
        if (kills.containsKey(uuid)) {
            kills.put(uuid, getKills(uuid) + increment);
        } else {
            kills.put(uuid, increment);
        }
    }

    public static void updateDeaths(UUID uuid, int increment) {
        // If deaths HashMap contains the uuid, getDeaths returns a Integer, otherwise getKills would return null, which would cause a error
        if (deaths.containsKey(uuid)) {
            deaths.put(uuid, getDeaths(uuid) + increment);
        } else {
            deaths.put(uuid, increment);
        }
    }


    // Get cache
    public static Integer getKills(UUID uuid) {
        return kills.getOrDefault(uuid, 0);
    }

    public static Integer getDeaths(UUID uuid) {
        return deaths.getOrDefault(uuid, 1);
    }


    // Remove a UUID from both hashmaps
    public static void removeUUID(UUID uuid) {
        try {
            kills.remove(uuid);
        } catch (NullPointerException exception) {
            // Leave it empty, this is to prevent unnecessary error spam
        }

        try {
            deaths.remove(uuid);
        } catch (NullPointerException exception) {
            // Leave it empty, this is to prevent unnecessary error spam
        }
    }
}
