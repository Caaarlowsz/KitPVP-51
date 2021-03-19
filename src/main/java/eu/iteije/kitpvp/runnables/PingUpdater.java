package eu.iteije.kitpvp.runnables;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.utils.Scoreboard;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingUpdater {

    private final KitPvP instance;

    public PingUpdater(KitPvP instance) {
        this.instance = instance;
    }

    @Deprecated
    public Integer start() {
        return instance.getServer().getScheduler().scheduleAsyncRepeatingTask(instance, () -> {
            for (Player player : instance.getServer().getOnlinePlayers()) {
                // Use reflection to make sure the plugin works on every version
                try {
                    Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                    int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);

                    Scoreboard.ping.put(player.getUniqueId(), ping);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException
                        | SecurityException | NoSuchFieldException exception) {
                    exception.printStackTrace();
                }

                Scoreboard.load(player);
            }
        }, 4 * 20L, 20L);
    }

}
