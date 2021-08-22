package polar.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import polar.ac.Polar;

public class AlertsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {

            final Player player = (Player) commandSender;
            final Polar instance = Polar.INSTANCE;

            if (player.hasPermission("alerts.command")) {
                if (player.hasMetadata("ALERTS_OFF")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou enabled the display of alerts"));
                    player.removeMetadata("ALERTS_OFF", instance);

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou disabled the display of alerts"));
                    player.setMetadata("ALERTS_OFF", new FixedMetadataValue(instance, true));
                }
            }

        }
        return false;
    }
}
