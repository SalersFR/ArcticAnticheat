package honeybadger.ac.commands;

import honeybadger.ac.gui.SettingsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HBSettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("hbsettings.command")) {
                SettingsGUI creation = new SettingsGUI().createNewGUI();
                creation.setItems();

                creation.open(player);
            }
        }
        return true;
    }
}
