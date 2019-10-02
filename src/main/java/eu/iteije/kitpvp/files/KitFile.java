package eu.iteije.kitpvp.files;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.pluginutils.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class KitFile {
    // Create file and fileconfiguration variable
    private File file;
    private FileConfiguration fileConfiguration;

    // Object reference

    /**
     * @param instance instance of KitPvP (main) class, which extends JavaPlugin
     */
    public KitFile(KitPvP instance, boolean startup) {
        if (startup) {
            loadDefault(instance);
        } else {
            this.file = new File(instance.getDataFolder(), "kits.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    // Save kits.yml file
    public void save() {
        // Try to save the kits.yml fileconfiguration
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    // Get kits.yml file
    public FileConfiguration get() {
        // Return kits.yml fileconfiguration
        return this.fileConfiguration;
    }

    // Reload kits.yml file
    public void reload() {
        // Try statement to prevent plugin failures
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Load kits.yml defaults (if file is deleted or corrupt)
    public void loadDefault(KitPvP instance) {
        this.file = new File(instance.getDataFolder(), "kits.yml");

        // Check whether the file exists or not
        // If not; file will be created and 'created-message' will be sent to the console
        // If so; loaded message will be sent to the console
        if (!file.exists()) {
            try {
                // Try creating the new file
                file.createNewFile();
                // Saving the default kits.yml from the KitPvP.jar
                instance.saveResource("kits.yml", true);
                // Send created confirmation to the console
                Message.sendToConsole("&c[KitPvP] &fKits.yml has been created!", false);
            } catch (IOException e) {
                // Error message (creating new file)
                Message.sendToConsole("&c[KitPvP] &fCouldn't create kits.yml file!", false);
            }
        } else {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file);

            // Send loaded confirmation to the console
            Message.sendToConsole("&c[KitPvP] &fKits.yml has been loaded!", false);
        }
    }
}
