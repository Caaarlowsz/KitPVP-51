package eu.iteije.kitpvp.npcs;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.npcs.traits.SpawnNpcTrait;
import eu.iteije.kitpvp.pluginutils.Message;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NpcModule {

    private final Map<String, NPC> joinNpcs;

    private final KitPvP instance;
    private final PluginFile mapFile;

    public NpcModule(KitPvP instance, PluginFile mapFile) {
        this.instance = instance;
        this.mapFile = mapFile;
        this.joinNpcs = new HashMap<>();

        this.loadNpcs(mapFile);
    }

    private void loadNpcs(PluginFile mapFile) {
        ConfigurationSection maps = mapFile.get().getConfigurationSection("maps");
        if (maps == null) return;

        Set<String> keys = maps.getKeys(false);

        for (String map : keys) {
            map = map.toLowerCase();

            Location location = this.getLocation(map);
            if (location != null) {
                this.placeNpc(map, location, false);
            } else {
                Message.sendToConsole("Unable to place NPC for map " + map + ": invalid location.", true);
            }
        }
    }

    public void placeNpc(String map, Location location, boolean save) {
        map = map.toLowerCase();
        if (this.joinNpcs.containsKey(map)) despawn(map);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "&dClick to join");
        npc.addTrait(new SpawnNpcTrait(instance, map + "_spawn"));
        npc.data().setPersistent(NPC.);

        npc.spawn(location, SpawnReason.PLUGIN);

        if (save) {
            ConfigurationSection mapData = mapFile.get().getConfigurationSection("maps." + map.toUpperCase());
            if (mapData == null) return;
            mapData.set("npc.world", location.getWorld().getName());
            mapData.set("npc.x", location.getX());
            mapData.set("npc.y", location.getY());
            mapData.set("npc.z", location.getZ());
            mapData.set("npc.pitch", location.getPitch());
            mapData.set("npc.yaw", location.getYaw());
            mapFile.save();
        }

        this.joinNpcs.put(map, npc);
    }

    private Location getLocation(String map) {
        map = map.toUpperCase();
        ConfigurationSection npcData = mapFile.get().getConfigurationSection("maps." + map + ".npc");
        if (npcData == null) return null;

        World world = Bukkit.getWorld(npcData.getString("world"));
        double x = npcData.getDouble("x");
        double y = npcData.getDouble("y");
        double z = npcData.getDouble("z");
        float yaw = Float.parseFloat(String.valueOf(npcData.getString("yaw")));
        float pitch = Float.parseFloat(String.valueOf(npcData.getString("pitch")));

        return new Location(world, x, y, z, yaw, pitch);

    }

    public void despawn(String map) {
        NPC npc = this.joinNpcs.get(map);
        if (npc != null) {
            npc.despawn(DespawnReason.PLUGIN);
            this.joinNpcs.remove(map);
        }
    }

    public void unload() {
        for (NPC npc : this.joinNpcs.values()) {
            npc.despawn(DespawnReason.PLUGIN);
        }
    }

//    private NPC setSkin(NPC npc) {
//
//    }

}
