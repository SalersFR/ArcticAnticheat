package polar.ac.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import polar.ac.gui.SettingsGUI;

public class PolarSettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("psettings.command")) {
                SettingsGUI creation = new SettingsGUI().createNewGUI();
                creation.setItems();

                creation.open(player);
            }
        }
        return true;
    }
}
