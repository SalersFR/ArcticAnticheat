package honeybadger.ac.commands;

import honeybadger.ac.HoneyBadger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HBReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("hbreload.command")) {
                HoneyBadger.INSTANCE.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&c&lHoneyBadger &fconfigurations have been reloaded."));
            }
        }
        return true;
    }
}
