package honeybadger.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HoneyBadgerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if (player.hasPermission("honeybadger.command")) {
                if (args.length <= 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lHoneyBadger\n" +
                            " \n&7 - &c/alerts &fToggle the display of flags\n&7 - &c/debug <check> &fEnable the debug"));

                } else {
                    player.sendMessage("&cUnknown command try /honeybadger");

                }
            }
        }
        return false;
    }
}
