package honeybadger.ac;

import honeybadger.ac.commands.AlertsCommand;
import honeybadger.ac.commands.DebugCommand;
import honeybadger.ac.commands.HoneyBadgerCommand;
import honeybadger.ac.data.PlayerDataManager;
import honeybadger.ac.listener.bukkit.JoinLeaveListener;
import honeybadger.ac.listener.packet.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HoneyBadger extends JavaPlugin {

    public static HoneyBadger INSTANCE;
    private PlayerDataManager dataManager = new PlayerDataManager();


    @Override
    public void onEnable() {
        INSTANCE = this;
        registerEvents();
        registerCommands();
        saveDefaultConfig();

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
        new PacketHandler();
    }

    private void registerCommands() {
        getCommand("honeybadger").setExecutor(new HoneyBadgerCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("debug").setExecutor(new DebugCommand());
    }


}
