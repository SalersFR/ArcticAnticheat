package dev.arctic.anticheat.commands.completion;


import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DebugCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final PlayerData data = Arctic.getInstance().getPlayerDataManager().getPlayerData(player);
            if (player.hasPermission("arctic.debug")) {
                List<String> returnList = new ArrayList<>();

                if (args.length >= 1) {
                    for (Check c : data.getCheckManager().getChecks()) {
                        returnList.add(c.getName() + c.getType());
                    }
                }
                return returnList;
            }
        }

        return null;
    }
}
