package arctic.ac.commands.impl;

import arctic.ac.Arctic;
import arctic.ac.gui.SettingsGUI;
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
            if (player.hasPermission("arctic.admin")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r \n&3&lArctic Advanced Anticheat\n" +
                            " \n&7 " +
                            "- &b/alerts [verbose] &fToggle the display of flags\n&7 " +
                            "- &b/debug <check> &fEnable the debug\n&7 " +
                            "- &b/arctic reload &fReload the config file\n&7 " +
                            "- &b/arctic settings &fToggle checks or autobans\n&7 " +
                            "- &b/info <player> &fCheck a player's information"));
                } else if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        Arctic.INSTANCE.reloadConfig();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&3&lArctic's &fconfigurations have been reloaded."));
                    } else if (args[0].equalsIgnoreCase("settings")) {
                        SettingsGUI creation = new SettingsGUI().createNewGUI();
                        creation.setItems();

                        creation.display(player);
                    }
                }
            }
        }
        return false;
    }
}
