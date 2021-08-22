package honeybadger.ac;

import honeybadger.ac.commands.*;
import honeybadger.ac.data.PlayerDataManager;
import honeybadger.ac.listener.bukkit.InventoryClickSettings;
import honeybadger.ac.listener.bukkit.JoinLeaveListener;
import honeybadger.ac.listener.packet.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class HoneyBadger extends JavaPlugin {

    public static HoneyBadger INSTANCE;
    private PlayerDataManager dataManager = new PlayerDataManager();


    public static ArrayList<LivingEntity> entities = new ArrayList<>();


    @Override
    public void onEnable() {
        INSTANCE = this;
        registerEvents();
        registerCommands();
        saveDefaultConfig();

        // Changes

        for (LivingEntity entity : Bukkit.getWorlds().get(0).getLivingEntities()) {
            entities.add(entity);
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
        getCommand("honeybadger").setExecutor(new HoneyBadgerCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("hbreload").setExecutor(new HBReloadCommand());
        getCommand("hbsettings").setExecutor(new HBSettingsCommand());
    }


}
