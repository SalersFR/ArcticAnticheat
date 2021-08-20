package honeybadger.ac.commands;

import honeybadger.ac.HoneyBadger;
import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final PlayerData data = HoneyBadger.INSTANCE.getDataManager().getPlayerData(player);
            if (player.hasPermission("debug.command")) {
                if (args.length <= 1) {
                    final HoneyBadger instance = HoneyBadger.INSTANCE;
                    final String toDebug = args[0];

                    for (Check checks : data.getChecks()) {
                        if (toDebug.equalsIgnoreCase(checks.getName() + checks.getType())) {
                            if (!player.hasMetadata(toDebug)) {
                                player.setMetadata(checks.getName() + checks.getType(), new FixedMetadataValue(instance, true));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lHoneyBadger&f | " +
                                        "&aNow debugging &7" + toDebug));
                            } else {
                                player.removeMetadata(toDebug, instance);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lHoneyBadger&f | " +
                                        "&cNo longer debugging &7" + toDebug));
                            }

                        }
                    }
                }
            }

        }

        return false;
    }
}
