package polar.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import polar.ac.Polar;

public class PolarReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("preload.command")) {
                Polar.INSTANCE.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&b&lPolar's &fconfigurations have been reloaded."));
            }
        }
        return true;
    }
}
