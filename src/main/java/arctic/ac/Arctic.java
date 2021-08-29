package arctic.ac;

import arctic.ac.commands.*;
import arctic.ac.data.PlayerDataManager;
import arctic.ac.listener.bukkit.InventoryClickSettings;
import arctic.ac.listener.bukkit.JoinLeaveListener;
import arctic.ac.listener.packet.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Arctic extends JavaPlugin {

    public static Arctic INSTANCE;
    private PlayerDataManager dataManager = new PlayerDataManager();

    private boolean citizensEnabled;

    @Override
    public void onEnable() {
        INSTANCE = this;
        registerEvents();
        registerCommands();
        saveDefaultConfig();

        // Changes

        if (Bukkit.getPluginManager().getPlugin("Citizens") == null) {
            citizensEnabled = false;
        } else {
            citizensEnabled = true;
        }

        Bukkit.broadcastMessage(citizensEnabled + "");
        for (Plugin s : Bukkit.getPluginManager().getPlugins()) {
            Bukkit.broadcastMessage(s.getName());
        }
    }


    @Override
    public void onDisable() {
        INSTANCE = null;
        this.dataManager = null;
    }

    public PlayerDataManager getDataManager() {
        return dataManager;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickSettings(), this);
        new PacketHandler();
    }

    private void registerCommands() {
        getCommand("arctic").setExecutor(new ArcticCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("areload").setExecutor(new ArcticReloadCommand());
        getCommand("asettings").setExecutor(new ArcticSettingsCommand());
    }

    public boolean isCitizensPresent() {
        return citizensEnabled;
    }
}
