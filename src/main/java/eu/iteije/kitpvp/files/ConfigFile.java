package eu.iteije.kitpvp.files;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    // Create file and fileconfiguration variable
    private File file;
    private FileConfiguration fileConfiguration;

    // Object reference

    /**
     * @param instance instance of KitPvP (main) class, which extends JavaPlugin
     */
    public ConfigFile(KitPvP instance, boolean startup) {
        if (startup) {
            loadDefault(instance);
        } else {
            this.file = new File(instance.getDataFolder(), "config.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    // Save config.yml file
    public void save() {
        // Try to save the config.yml fileconfiguration
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    // Get config.yml file
    public FileConfiguration get() {
        // Return config.yml fileconfiguration
        return this.fileConfiguration;
    }

    // Reload config.yml file
    public void reload() {
        // Try statement to prevent plugin failures
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Load config.yml defaults (if file is deleted or corrupt)
    public void loadDefault(KitPvP instance) {
        this.file = new File(instance.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                instance.saveResource("config.yml", true);
                Message.sendToConsole("&c[KitPvP] &fConfig.yml has been created!", false);
            } catch (IOException e) {
                Message.sendToConsole("&c[KitPvP] &fCouldn't create config.yml file!", false);
            }
        } else {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
            instance.saveResource("config.yml", false);
            Message.sendToConsole("&c[KitPvP] &fConfig.yml has been loaded!", false);
        }
    }
}
