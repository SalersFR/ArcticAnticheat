package arctic.ac.commands;

import arctic.ac.Arctic;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArcticReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("areload.command")) {
                Arctic.INSTANCE.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&b&lArctic's &fconfigurations have been reloaded."));
            }
        }
        return true;
    }
}
