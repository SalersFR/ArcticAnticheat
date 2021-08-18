package honeybadger.ac.commands;

import honeybadger.ac.HoneyBadger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class AlertsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {

            final Player player = (Player) commandSender;
            final HoneyBadger instance = HoneyBadger.INSTANCE;

            if (player.hasPermission("alerts.command")) {
                if (player.hasMetadata("ALERTS_OFF")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou enabled the display of alerts"));
                    player.removeMetadata("ALERTS_OFF", instance);

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled the display of alerts"));
                    player.setMetadata("ALERTS_OFF", new FixedMetadataValue(instance, true));
                }
            }

        }
        return false;
    }
}
