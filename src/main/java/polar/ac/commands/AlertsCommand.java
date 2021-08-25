package polar.ac.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import polar.ac.Polar;
import polar.ac.utils.CustomUtils;

public class AlertsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {

            final Player player = (Player) commandSender;
            final Polar instance = Polar.INSTANCE;

            // player.removeMetadata("ALERTS_OFF", instance);
            // player.setMetadata("ALERTS_OFF", new FixedMetadataValue(instance, true));

            if (player.hasPermission("alerts.command")) {
                if (args.length == 0) {
                    // Player has typed /alerts

                    if (player.hasMetadata("ALERTS_ON_VERBOSE")) {
                        player.removeMetadata("ALERTS_ON_VERBOSE", instance);
                        player.sendMessage(CustomUtils.translate("&bVerbose &balerts have been disabled."));
                        // msg
                        return true;
                    }

                    if (player.hasMetadata("ALERTS_ON_NORMAL")) {
                        player.removeMetadata("ALERTS_ON_NORMAL", instance);
                        player.sendMessage(CustomUtils.translate("&bAlerts have been disabled."));
                        // msg
                        return true;
                    } else {
                        if (player.hasMetadata("ALERTS_ON_VERBOSE")) player.removeMetadata("ALERTS_ON_VERBOSE", instance);
                        player.setMetadata("ALERTS_ON_NORMAL", new FixedMetadataValue(instance, true));
                        // msg
                        player.sendMessage(CustomUtils.translate("&aAlerts have been enabled."));
                        return true;
                    }
                } else if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("verbose")) {
                        if (player.hasMetadata("ALERTS_ON_NORMAL")) player.removeMetadata("ALERTS_ON_NORMAL", instance);
                        if (player.hasMetadata("ALERTS_ON_VERBOSE")) {
                            player.removeMetadata("ALERTS_ON_VERBOSE", instance);
                            // msg
                            player.sendMessage(CustomUtils.translate("&bVerbose &balerts have been disabled."));
                        } else {
                            player.setMetadata("ALERTS_ON_VERBOSE", new FixedMetadataValue(instance, true));
                            player.sendMessage(CustomUtils.translate("&bVerbose &aalerts have been enabled."));
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }
}
