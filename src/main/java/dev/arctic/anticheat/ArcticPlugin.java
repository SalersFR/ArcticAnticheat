package dev.arctic.anticheat;

import org.bukkit.plugin.java.JavaPlugin;

public class ArcticPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Arctic.createInstance();
        Arctic.getInstance().start(this);
    }

    @Override
    public void onDisable() {
        Arctic.getInstance().stop();


    }
}
