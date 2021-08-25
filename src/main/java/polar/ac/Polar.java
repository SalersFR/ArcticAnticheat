package polar.ac;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import polar.ac.commands.*;
import polar.ac.data.PlayerDataManager;
import polar.ac.listener.bukkit.InventoryClickSettings;
import polar.ac.listener.bukkit.JoinLeaveListener;
import polar.ac.listener.packet.PacketHandler;

public class Polar extends JavaPlugin {

    public static Polar INSTANCE;
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
        getCommand("polar").setExecutor(new PolarCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("preload").setExecutor(new PolarReloadCommand());
        getCommand("psettings").setExecutor(new PolarSettingsCommand());
    }

    public boolean isCitizensPresent() {
        return citizensEnabled;
    }
}
