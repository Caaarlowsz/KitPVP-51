package eu.iteije.kitpvp.files;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageFile {

    // Create file and fileconfiguration variable
    private File file;
    private FileConfiguration fileConfiguration;

    // Object reference

    /**
     * @param instance instance of KitPvP (main) class, which extends JavaPlugin
     */
    public MessageFile(KitPvP instance, boolean startup) {
        if (startup) {
            this.file = new File(instance.getDataFolder(), "messages.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
            loadDefault(instance);
        } else {
            this.file = new File(instance.getDataFolder(), "messages.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    // Save messages.yml file
    public void save() {
        // Try to save the messages.yml fileconfiguration
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    // Get messages.yml file
    public FileConfiguration get() {
        // Return messages.yml fileconfiguration
        return this.fileConfiguration;
    }

    // Reload messages.yml file
    public void reload() {
        // Try statement to prevent plugin failures
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Load messages.yml defaults (if file is deleted or corrupt)
    public void loadDefault(KitPvP instance) {
        // If plugin foler doesn't exists, create it
        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }

        this.file = new File(instance.getDataFolder(), "messages.yml");

        // Check whether the file exists or not
        // If not; file will be created and 'created-message' will be sent to the console
        // If so; loaded message will be sent to the console
        if (!file.exists()) {
            try {
                // Try creating the new file
                file.createNewFile();
                // Saving the default messages.yml from the KitPvP.jar
                instance.saveResource("messages.yml", true);
                // Send created confirmation to the console
                Bukkit.getConsoleSender().sendMessage("§c[KitPvP] §fMessages.yml has been created!");
            } catch (IOException e) {
                // Error message (creating new file)
                Bukkit.getConsoleSender().sendMessage("§c[KitPvP] §fCouldn't create messages.yml file!");
            }
        } else {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file);

            // Send loaded confirmation to the console
            Bukkit.getConsoleSender().sendMessage("§c[KitPvP] §fMessages.yml has been loaded!");
        }
    }
}
