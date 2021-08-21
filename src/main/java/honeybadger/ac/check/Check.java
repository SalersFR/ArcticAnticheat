package honeybadger.ac.check;

import honeybadger.ac.HoneyBadger;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;



public abstract class Check {

    public final PlayerData data;
    private final String name, configName, type;
    private final boolean experimental;
    public double buffer = 0, vl = 0;

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
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("alerts.see") && !player.hasMetadata("ALERTS_OFF")) {

                vl++;
                final String fromConfig = HoneyBadger.INSTANCE.getConfig().getString("flag-message").
                        replace("%player%", data.getBukkitPlayerFromUUID().getName()).
                        replace("%vl%", "" + vl).
                        replace("%type%", type).
                        replace("%check%", name);

                final TextComponent alertMSG = new TextComponent(ChatColor.translateAlternateColorCodes('&',fromConfig));

                alertMSG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.
                        translateAlternateColorCodes('&',"&c&lHoneyBadger\n&7 \n&7Info: &c" + info +
                                "\n &7\n&7Experimental: &c" + experimental + "\n &f\n&fClick to teleport !")).create()));

                alertMSG.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp " + data.getPlayer().getName()));


                if (vl > getBanVL()) {
                    if (isPunish()) {

                        final String toDispatch = HoneyBadger.INSTANCE.getConfig().getString("ban-command").
                                replace("%player%", data.getBukkitPlayerFromUUID().getName()).
                                replace("%vl%", "" + vl).
                                replace("%type%", type).
                                replace("%check%", name);


                        Bukkit.getScheduler().runTask(HoneyBadger.INSTANCE,() -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', toDispatch));
                            for (Check checks : data.getChecks()) {
                                checks.vl = 0;
                            }
                        });
                    }
                }

                player.spigot().sendMessage(alertMSG);
            }
        }


    }

    protected void debug(String debug) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasMetadata(name + type)) {
                players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c&lDEBUG&8] &7" + debug));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getConfigName() {
        return configName;
    }

    public String getType() {
        return type;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public int getBanVL() {
        return HoneyBadger.INSTANCE.getConfig().getInt("checks." + this.getConfigName() + ".ban-vl");
    }

    public boolean isEnabled() {
        return HoneyBadger.INSTANCE.getConfig().getBoolean("checks." + this.getConfigName() + ".enabled");
    }

    public boolean isPunish() {
        return HoneyBadger.INSTANCE.getConfig().getBoolean("checks." + this.getConfigName() + ".punish");
    }
}
