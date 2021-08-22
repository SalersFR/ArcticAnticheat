package polar.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import polar.ac.Polar;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final PlayerData data = Polar.INSTANCE.getDataManager().getPlayerData(player);
            if (player.hasPermission("debug.command")) {
                if (args.length >= 1) {
                    final Polar instance = Polar.INSTANCE;
                    final String toDebug = args[0];

                    for (Check checks : data.getChecks()) {
                        if (toDebug.equalsIgnoreCase(checks.getName() + checks.getType())) {
                            if (!player.hasMetadata(toDebug)) {
                                player.setMetadata(checks.getName() + checks.getType(), new FixedMetadataValue(instance, true));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lPolar&f | " +
                                        "&aNow debugging &7" + toDebug));
                            } else {
                                player.removeMetadata(checks.getName() + checks.getType(), instance);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lPolar&f | " +
                                        "&bNo longer debugging &7" + toDebug));
                            }

                        }
                    }
                }
            }

        }

        return false;
    }
}
