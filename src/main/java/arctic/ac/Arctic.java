package arctic.ac;

import arctic.ac.commands.completion.AlertsCompletion;
import arctic.ac.commands.completion.ArcticCompletion;
import arctic.ac.commands.completion.DebugCompletion;
import arctic.ac.commands.impl.AlertsCommand;
import arctic.ac.commands.impl.ArcticCommand;
import arctic.ac.commands.impl.ArcticInfoCommand;
import arctic.ac.commands.impl.DebugCommand;
import arctic.ac.data.PlayerDataManager;
import arctic.ac.file.CheckFileManager;
import arctic.ac.listener.bukkit.InventoryClickSettings;
import arctic.ac.listener.bukkit.JoinLeaveListener;
import arctic.ac.listener.packet.PacketHandler;
import arctic.ac.utils.CustomUtils;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Arctic extends JavaPlugin {

    public static Arctic INSTANCE;
    private PlayerDataManager dataManager = new PlayerDataManager();

    private CheckFileManager checkFileManager;

    @Getter
    private ExecutorService checksThread = Executors.newSingleThreadExecutor();

    @Getter
    private ExecutorService alertThread = Executors.newSingleThreadExecutor();

    @Getter
    private ExecutorService dataThread = Executors.newSingleThreadExecutor();


    private boolean citizensEnabled;

    @Override
    public void onLoad() {
        CustomUtils.consoleLog("&bLoading Arctic b1-BETA");

        PacketEvents.create(this).getSettings()
                .bStats(true)
                .checkForUpdates(false)
                .compatInjector(false)
                .fallbackServerVersion(ServerVersion.v_1_8_8);

        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        CustomUtils.consoleLog("&bEnabling Arctic b1-BETA");
        CustomUtils.consoleLog("&bArctic AntiCheat - Developed by &eSalers&b, &exWand&b, &eDerRedstoner&b and &eCrafticat.");
        CustomUtils.consoleLog("&bLoading checks and modules...");
        CustomUtils.consoleLog("&bLoading configuration files...");
        checkFileManager = new CheckFileManager(this);
        saveDefaultConfig();
        INSTANCE = this;
        CustomUtils.consoleLog("&bRegistering events and listeners...");
        registerEvents();
        registerCommands();

        PacketEvents.get().init();
        PacketEvents.get().registerListener(new PacketHandler());


        // Changes

        if (Bukkit.getPluginManager().getPlugin("Citizens") == null) {
            citizensEnabled = false;
        } else {
            citizensEnabled = true;
        }

        for (Plugin s : Bukkit.getPluginManager().getPlugins()) {
            Bukkit.broadcastMessage(s.getName());
        }

        CustomUtils.consoleLog("&bSuccessfully enabled.");
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
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "MC|Brand", new JoinLeaveListener());

    }

    private void registerCommands() {
        getCommand("arctic").setExecutor(new ArcticCommand());
        getCommand("arctic").setTabCompleter(new ArcticCompletion());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("alerts").setTabCompleter(new AlertsCompletion());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("debug").setTabCompleter(new DebugCompletion());
        getCommand("info").setExecutor(new ArcticInfoCommand());
    }

    public boolean isCitizensPresent() {
        return citizensEnabled;
    }
}
