package arctic.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ArcticCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if (player.hasPermission("arctic.command")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r \n&3&lArctic Advanced Anticheat\n" +
                        " \n&7 - &3/alerts [verbose] &fToggle the display of flags\n&7 -" +
                        " &3/debug <check> &fEnable the debug\n&7 - &3/areload &fReload the config file\n&7 - &3/asettings &fToggle checks or autobans"));
            }
        }
        return false;
    }
}
