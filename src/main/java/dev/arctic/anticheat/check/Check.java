package dev.arctic.anticheat.check;


import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.CustomUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public abstract class Check {

    public final PlayerData data;
    private final String name, configName, type, desc;
    private final boolean experimental;
    public double buffer = 0, vl = 0;


    public Check(PlayerData data, String name, String type, String configName, String desc, boolean experimental) {

        this.data = data;

        this.name = name;
        this.type = type;
        this.configName = configName;
        this.experimental = experimental;
        this.desc = desc;

    }

    public abstract void handle(final Packet packet, final long time);


    protected void fail() {
        fail("Not any Information provided.");
    }

    protected void fail(String info) {
        vl++;

        if (canSetback())
            data.getCancelProcessor().setMovementCancel(true);


        final String prefix = ChatColor.translateAlternateColorCodes('&', Arctic.getInstance().getPlugin().getConfig().getString("prefix"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("alerts.see") && player.hasMetadata("ALERTS_ON_NORMAL")) {
                final String fromConfig = ChatColor.translateAlternateColorCodes('&',
                        Arctic.getInstance().getPlugin().getConfig().getString("flag-message").
                                replace("%player%", data.getPlayer().getName()).
                                replace("%vl%", "" + ((int) vl)).
                                replace("%maxvl%", "" + getBanVL()).
                                replace("%type%", type).
                                replace("%check%", name));
                final TextComponent alertMSG = new TextComponent(fromConfig);

                alertMSG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.
                        translateAlternateColorCodes('&', "&b&lArctic\n&7 \n&7Info: &b" + info +
                                "\n &7\n&7Experimental: &b" + experimental + "\n &f\n&fClick to teleport.")).create()));

                alertMSG.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + data.getPlayer().getName()));

                TextComponent prefixComp = new TextComponent(prefix + "");
                prefixComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(CustomUtils.translate("&b&lArctic &r&bCheat Detection")).create()));
                player.spigot().sendMessage(prefixComp, alertMSG);
            } else if (player.hasPermission("alerts.see") && player.hasMetadata("ALERTS_ON_VERBOSE")) {
                final String fromConfig = Arctic.getInstance().getPlugin().getConfig().getString("flag-message-verbose").
                        replace("%player%", data.getPlayer().getName()).
                        replace("%vl%", "" + vl).
                        replace("%type%", type).
                        replace("%check%", name).
                        replace("%ping%", data.getConnectionProcessor().getKeepAlivePing() + "").
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

                    final String toDispatch = Arctic.getInstance().getPlugin().getConfig().getString("ban-command").
                            replace("%player%", data.getPlayer().getName()).
                            replace("%vl%", "" + vl).
                            replace("%type%", type).
                            replace("%check%", name).
                            replace("%prefix%", prefix);


                    Bukkit.getScheduler().runTask(Arctic.getInstance().getPlugin(), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', toDispatch));
                    });

                    Bukkit.broadcastMessage(CustomUtils.translate("&m&l-----------------------------------------"));
                    Bukkit.broadcastMessage(CustomUtils.translate("&b&lArctic &r&bremoved &c" + data.getPlayer()
                            .getName() + " &bfor &cUnfair Advantage."));
                    Bukkit.broadcastMessage(CustomUtils.translate("&m&l-----------------------------------------"));

                    if (Arctic.getInstance().getPlugin().getConfig().getBoolean("ban-animation.blood"))
                        blood(data.getPlayer());

                    if(Arctic.getInstance().getPlugin().getConfig().getBoolean("ban-animation.lightning"))
                        data.getPlayer().getWorld().strikeLightningEffect(data.getPlayer().getLocation());


                    for (Check checks : data.getCheckManager().getChecks()) {
                        checks.vl = 0;
                    }
                }
            }
        }


    }

    private void blood(Player player) {
        ItemStack redBlood = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
        for (int i = 0; i < 8; i++) {
            Item item = player.getWorld().dropItemNaturally(player.getEyeLocation().subtract(0, 0.2, 0), redBlood);
            item.getItemStack().getItemMeta().setDisplayName(CustomUtils.translate("Blood"));
            item.setPickupDelay(1000000);

            Bukkit.getScheduler().runTaskLater(Arctic.getInstance().getPlugin(), () -> {
                if (item.isValid()) item.remove();
            }, 20 * 3);
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
        return Arctic.getInstance().getPlugin().getConfig().getInt("checks." + this.getConfigName() + ".ban-vl");
    }

    public boolean isEnabled() {
        return Arctic.getInstance().getPlugin().getConfig().getBoolean("checks." + this.getConfigName() + ".enabled");
    }

    public boolean isPunish() {
        return Arctic.getInstance().getPlugin().getConfig().getBoolean("checks." + this.getConfigName() + ".punish");
    }

    public boolean isSetback() {
        return Arctic.getInstance().getPlugin().getConfig().getBoolean("checks." + this.getConfigName() + ".setback");
    }

    public boolean canSetback() {
        return !data.getCancelProcessor().isMovementCancel() && configName.contains("movement") && !configName.contains("nofall");
    }


}