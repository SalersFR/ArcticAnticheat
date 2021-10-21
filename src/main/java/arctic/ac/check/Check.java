package arctic.ac.check;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.utils.CustomUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


public abstract class Check {

    public final PlayerData data;
    private final String name, configName, type,desc;
    private final boolean experimental;
    public double buffer = 0, vl = 0;


    public Check(PlayerData data, String name, String type, String configName,String desc, boolean experimental) {

        this.data = data;

        this.name = name;
        this.type = type;
        this.configName = configName;
        this.experimental = experimental;
        this.desc = desc;

    }

    public abstract void handle(Event e);

    protected void fail() {
        fail("Not any Information provided.");
    }

    protected void fail(String info) {
        vl++;

        final String loweredName = this.configName.toLowerCase();

        if (loweredName.contains("movement") && this.isSetback())
            data.getSetbackProcessor().setback();

        final String prefix = CustomUtils.translate(Arctic.INSTANCE.getConfig().getString("prefix"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("alerts.see") && player.hasMetadata("ALERTS_ON_NORMAL")) {
                final String fromConfig = CustomUtils.translate(Arctic.INSTANCE.getConfig().getString("flag-message").
                        replace("%player%", data.getBukkitPlayerFromUUID().getName()).
                        replace("%vl%", "" + ((int) vl)).
                        replace("%maxvl%", "" + getBanVL()).
                        replace("%type%", type).
                        replace("%check%", name).
                        replace("%prefix%", prefix));
                final TextComponent alertMSG = new TextComponent(fromConfig);

                alertMSG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.
                        translateAlternateColorCodes('&', "&b&lArctic\n&7 \n&7Info: &b" + info +
                                "\n &7\n&7Experimental: &b" + experimental + "\n &f\n&fClick to teleport.")).create()));

                alertMSG.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + data.getPlayer().getName()));

                TextComponent prefixComp = new TextComponent(prefix);
                prefixComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CustomUtils.translate("&b&lArctic &r&bCheat Detection")).create()));
                player.spigot().sendMessage(prefixComp, alertMSG);
            } else if (player.hasPermission("alerts.see") && player.hasMetadata("ALERTS_ON_VERBOSE")) {
                final String fromConfig = Arctic.INSTANCE.getConfig().getString("flag-message-verbose").
                        replace("%player%", data.getBukkitPlayerFromUUID().getName()).
                        replace("%vl%", "" + vl).
                        replace("%type%", type).
                        replace("%check%", name).
                        replace("%ping%", ((CraftPlayer) data.getBukkitPlayerFromUUID()).getHandle().ping + "").
                        replace("%tps%", MinecraftServer.getServer().recentTps[0] + "").
                        replace("%prefix%", prefix);

                final TextComponent alertMSG = new TextComponent(ChatColor.translateAlternateColorCodes('&', fromConfig));

                alertMSG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.
                        translateAlternateColorCodes('&', "&b&lArctic\n&7 \n&7Info: &b" + info +
                                "\n &7\n&7Experimental: &b" + experimental + "\n &f\n&fClick to teleport.")).create()));

                alertMSG.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + data.getPlayer().getName()));

                player.spigot().sendMessage(alertMSG);
            }

            if (vl >= getBanVL()) {
                if (isPunish()) {

                    final String toDispatch = Arctic.INSTANCE.getConfig().getString("ban-command").
                            replace("%player%", data.getBukkitPlayerFromUUID().getName()).
                            replace("%vl%", "" + vl).
                            replace("%type%", type).
                            replace("%check%", name).
                            replace("%prefix%", prefix);


                    Bukkit.getScheduler().runTask(Arctic.INSTANCE, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', toDispatch));
                        for (Check checks : data.getChecks()) {
                            checks.vl = 0;
                        }
                    });
                }
            }
        }


    }

    protected void debug(String debug) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasMetadata(name + type)) {
                players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&b&lDEBUG&8] &7" + debug));
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

    public String getDesc() {
        return desc;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public int getBanVL() {
        return Arctic.INSTANCE.getConfig().getInt("checks." + this.getConfigName() + ".ban-vl");
    }

    public boolean isEnabled() {
        return Arctic.INSTANCE.getConfig().getBoolean("checks." + this.getConfigName() + ".enabled");
    }

    public boolean isPunish() {
        return Arctic.INSTANCE.getConfig().getBoolean("checks." + this.getConfigName() + ".punish");
    }

    public boolean isSetback() {
        return Arctic.INSTANCE.getConfig().getBoolean("checks." + this.getConfigName() + ".setback");
    }


}
