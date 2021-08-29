package arctic.ac.commands;

import arctic.ac.gui.SettingsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArcticSettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("asettings.command")) {
                SettingsGUI creation = new SettingsGUI().createNewGUI();
                creation.setItems();

                creation.display(player);
            }
        }
        return true;
    }
}
