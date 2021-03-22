package eu.iteije.kitpvp.npcs;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.files.PluginFile;
import eu.iteije.kitpvp.npcs.listeners.NPCClickListener;
import eu.iteije.kitpvp.pluginutils.Message;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
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

        instance.getServer().getPluginManager().registerEvents(new NPCClickListener(instance), instance);

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
        if (this.joinNpcs.containsKey(map)) delete(map);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "&dClick to join");
        npc.data().setPersistent("map", map.toUpperCase());
        this.setSkin(npc);

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

    public void delete(String map) {
        NPC npc = this.joinNpcs.get(map);
        if (npc != null) {
            npc.destroy();
            CitizensAPI.getNPCRegistry().deregister(npc);
            this.joinNpcs.remove(map);
        }
    }

    public void unload() {
        CitizensAPI.getNPCRegistry().deregisterAll();
    }

    private NPC setSkin(NPC npc) {
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(
                "639601793",
                "x7fGrW9QImG5fIVOpPiMqXCAEJKqUFck/yAqkY+xCyaFIVHJz9K/q+3vf/ESVfTbGoVU9yt2d1FKAb3OrV0G/tfmz55TFa3TDQdBGweGiViaexPNwPHKHxlychvSqD6DKWofuUOdhJIwzx+uraGU4310oZ6JeKxWuAuBJZtSDBBn+Vlfriqsh7CrZ43bupDENu67iMTuuJI/e2NoKYUumG16TYJI4CdtiRPQHM4kQhDQWma/CjCxzdaLTeZg14uJoR3TUEmsD35yzyhm9iU4eHia3Oo7YTYVnGZTlkJqYODfhUZB6D+FspV9RuLEAawQB6hRKcKoQvTdEQF05FzyZwY72lhofeAKNnhmQQXgxQWWZpJqvSgAP4webO6btCaTdRGmkuuteIP85abUjHUMMxz6uYiEWoeh3FvQ9IChz9UZPRf4i6wiPMOuedJAbnL+n3wXYr+7+6lbfqhK6Q6YRe4MC8ouwb4SSUJ8KNuBhkCK7438up2fA+yMVwABcckJ6yVHoyAneAmPJpUvT76qzZsPDYv9QVtrUOpquS26eDlF1pVI4S5LbdsakYRJFr1CwMduGdGeY1xYnAdM+MBwBzHjQdYfsNJHUWt1ze28UUTOZrZVAcpXoVFD4bfL5gi35Ob/sBUECgzfM/p2ZH5h2ulby7dX3c9F3vNDVkGyDP8=",
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMTI4NjE1MTk1NywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NjRlMWEzODg5YzFiNDI5NzgwNDE2MDY0NTY4NGE1NGE2ZGNlY2VmMzc1Nzc5OWRlYTVmMzc3NDNhMWI1MjY0IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
        );
        return npc;
    }

}
