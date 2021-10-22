package arctic.ac.file;

import arctic.ac.Arctic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class CheckFileManager {

    private Arctic instance;
    private FileConfiguration config = null;
    private File configFile = null;

    public CheckFileManager(Arctic instance){
        this.instance = instance;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.instance.getDataFolder(), "checks.yml");

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.instance.getResource("checks.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadConfig();

        return this.config;
    }

    public void saveConfig() {
        if (this.config == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            Arctic.INSTANCE.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.instance.getDataFolder(), "checks.yml");

        if (!this.configFile.exists()) {
            this.instance.saveResource("checks.yml", false);
        }
    }
}
