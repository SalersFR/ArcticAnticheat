package honeybadger.ac.check;

import honeybadger.ac.HoneyBadger;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Check {

    public final PlayerData data;
    private final String name, configName, type;
    private final boolean experimental;
    public double buffer = 0,vl = 0;

    public Check(PlayerData data, String name, String type, String configName, boolean experimental) {

        this.data = data;

        this.name = name;
        this.type = type;
        this.configName = configName;
        this.experimental = experimental;

    }

    public abstract void handle(Event e);

    protected void fail() {
        fail("Not any Information provided.");
    }

    protected void fail(String info) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("alerts.see") && !player.hasMetadata("ALERTS_OFF")) {

                vl++;
                final String fromConfig = HoneyBadger.INSTANCE.getConfig().getString("flag-message").
                        replace("%player%",data.getBukkitPlayerFromUUID().getName()).
                        replace("%vl%","" + vl).
                        replace("%type%",type).
                        replace("%check%",name);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&',fromConfig));
            }
        }


    }

    protected void debug(String debug) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasMetadata(name + type)) {
                players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDEBUG &r" + debug));
            }
        }
    }
}
