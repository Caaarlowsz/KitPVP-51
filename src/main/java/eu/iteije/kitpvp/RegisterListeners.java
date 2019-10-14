package eu.iteije.kitpvp;

import eu.iteije.kitpvp.listeners.*;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {

    // Instance variable of main class
    private KitPvP instance;

    public RegisterListeners(KitPvP instance) {
        this.instance = instance;
        registerListeners();
    }

    // Register all listeners
    private void registerListeners() {
        PluginManager pluginManager = instance.getServer().getPluginManager();

        // PlayerInteractEvent
        pluginManager.registerEvents(new PlayerInteract(instance), instance);
        // SignChangeEvent
        pluginManager.registerEvents(new SignChange(instance), instance);
        // BlockPlaceEvent
        pluginManager.registerEvents(new BlockPlace(instance), instance);
        // PlayerQuitEvent
        pluginManager.registerEvents(new PlayerQuit(instance), instance);
        // InventoryClickEvent
        pluginManager.registerEvents(new InventoryClick(instance), instance);
        // PlayerDropItemEvent
        pluginManager.registerEvents(new PlayerDropItem(instance), instance);
        // PlayerPickupItemEvent
        pluginManager.registerEvents(new PlayerPickupItem(instance), instance);
        // BlockBreakEvent
        pluginManager.registerEvents(new BlockBreak(instance), instance);
        // PlayerSwapHandItemsEvent
        pluginManager.registerEvents(new PlayerSwapHandItems(instance), instance);
        // PlayerJoinEvent
        pluginManager.registerEvents(new PlayerJoin(instance), instance);
        // PlayerGameModeChangeEvent
        pluginManager.registerEvents(new PlayerGameModeChange(instance), instance);
        // FoodLevelChangeEvent
        pluginManager.registerEvents(new FoodLevelChange(instance), instance);
        // InventoryCloseEvent
        pluginManager.registerEvents(new InventoryClose(instance), instance);
        // PlayerDeathEvent
        pluginManager.registerEvents(new PlayerDeath(instance), instance);
        // PlayerRespawnEvent
        pluginManager.registerEvents(new PlayerRespawn(instance), instance);
        // EntityDamageByEntityEvent
        pluginManager.registerEvents(new EntityDamageByEntity(instance), instance);
        // AsyncPlayerChatEvent
        pluginManager.registerEvents(new AsyncPlayerChat(instance), instance);
    }

}
