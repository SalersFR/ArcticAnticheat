package arctic.ac.commands.completion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArcticCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("arctic.admin")) {
                List<String> returnList = new ArrayList<>();

                if (args.length >= 1) {
                    returnList.add("reload");
                    returnList.add("settings");
                }
                return returnList;
            }
        }

        return null;
    }
}
