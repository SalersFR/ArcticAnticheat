package dev.arctic.anticheat;

import dev.arctic.anticheat.commands.completion.AlertsCompletion;
import dev.arctic.anticheat.commands.completion.ArcticCompletion;
import dev.arctic.anticheat.commands.completion.DebugCompletion;
import dev.arctic.anticheat.commands.impl.AlertsCommand;
import dev.arctic.anticheat.commands.impl.ArcticCommand;
import dev.arctic.anticheat.commands.impl.ArcticInfoCommand;
import dev.arctic.anticheat.commands.impl.DebugCommand;
import dev.arctic.anticheat.listener.InventoryClickSettings;
import dev.arctic.anticheat.listener.PlayerListener;
import dev.arctic.anticheat.manager.PlayerDataManager;
import dev.arctic.anticheat.listener.PacketListener;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Arctic {
    private static Arctic instance;

    private ArcticPlugin plugin;

    private final PlayerDataManager playerDataManager = new PlayerDataManager();

    private final ExecutorService checkService = Executors.newSingleThreadExecutor();
    private final ExecutorService dataService = Executors.newSingleThreadExecutor();

    public static void createInstance() {
        instance = new Arctic();
    }

    public static Arctic getInstance() {
        return instance;
    }

    //we call the method just down in the onEnable

    public void start(final ArcticPlugin plugin) {
        this.plugin = plugin;

        //config registering
        plugin.saveDefaultConfig();


        registerCommands();
        registerEvents();


        new PacketListener(plugin);
    }

    //we call the method just down in the onDisable

    public void stop() {

        //doing that for preventing memory leaks

        this.plugin = null;
        instance = null;
        this.playerDataManager.getAllData().clear();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryClickSettings(), plugin);


    }

    private void registerCommands() {
        plugin.getCommand("arctic").setExecutor(new ArcticCommand());
        plugin.getCommand("arctic").setTabCompleter(new ArcticCompletion());
        plugin.getCommand("alerts").setExecutor(new AlertsCommand());
        plugin.getCommand("alerts").setTabCompleter(new AlertsCompletion());
        plugin.getCommand("debug").setExecutor(new DebugCommand());
        plugin.getCommand("debug").setTabCompleter(new DebugCompletion());
        plugin.getCommand("info").setExecutor(new ArcticInfoCommand());
    }
}
