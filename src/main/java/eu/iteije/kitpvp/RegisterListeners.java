package eu.iteije.kitpvp;

import eu.iteije.kitpvp.listeners.*;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {

    public RegisterListeners(KitPvP instance) {
        registerListeners(instance);
    }

    // Register all listeners
    private void registerListeners(KitPvP instance) {
        PluginManager pluginManager = instance.getServer().getPluginManager();

        // PlayerInteractEvent
        pluginManager.registerEvents(new PlayerInteract(instance), instance);
        // SignChangeEvent
        pluginManager.registerEvents(new SignChange(instance), instance);
        // BlockPlaceEvent
        pluginManager.registerEvents(new BlockPlace(), instance);
        // PlayerQuitEvent
        pluginManager.registerEvents(new PlayerQuit(), instance);
        // InventoryClickEvent
        pluginManager.registerEvents(new InventoryClick(instance), instance);
        // PlayerDropItemEvent
        pluginManager.registerEvents(new PlayerDropItem(), instance);
        // PlayerPickupItemEvent
        pluginManager.registerEvents(new PlayerPickupItem(), instance);
        // BlockBreakEvent
        pluginManager.registerEvents(new BlockBreak(instance), instance);
        // PlayerJoinEvent
        pluginManager.registerEvents(new PlayerJoin(instance), instance);
        // PlayerGameModeChangeEvent
        pluginManager.registerEvents(new PlayerGameModeChange(), instance);
        // FoodLevelChangeEvent
        pluginManager.registerEvents(new FoodLevelChange(instance), instance);
        // InventoryCloseEvent
        pluginManager.registerEvents(new InventoryClose(instance), instance);
        // PlayerDeathEvent
        pluginManager.registerEvents(new PlayerDeath(), instance);
        // PlayerRespawnEvent
        pluginManager.registerEvents(new PlayerRespawn(instance), instance);
        // EntityDamageByEntityEvent
        pluginManager.registerEvents(new EntityDamageByEntity(), instance);
        // AsyncPlayerChatEvent
        pluginManager.registerEvents(new AsyncPlayerChat(), instance);
    }

}
