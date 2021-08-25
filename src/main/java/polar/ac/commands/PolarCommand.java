package polar.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class PolarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if (player.hasPermission("polar.command")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r \n&b&lPolar Advanced Anticheat\n" +
                        " \n&7 - &b/alerts [verbose] &fToggle the display of flags\n&7 - &b/debug <check> &fEnable the debug\n&7 - &b/preload &fReload the config file\n&7 - &b/psettings &fToggle checks or autobans"));
            }
        }
        return false;
    }
}
