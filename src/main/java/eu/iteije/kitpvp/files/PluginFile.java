package eu.iteije.kitpvp.files;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginFile {

    private final FileConfiguration configuration;
    private final File file;

    public PluginFile(KitPvP instance, String fileName) {
        this.loadFile(instance, fileName);

        this.file = new File(instance.getDataFolder(), fileName);
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return this.configuration;
    }

    private void loadFile(KitPvP instance, String fileName) {
        if (!instance.getDataFolder().exists()) instance.getDataFolder().mkdir();

        File file = new File(instance.getDataFolder(), fileName);

        if (!file.exists()) {
            instance.saveResource(fileName, true);
        }
    }

}
