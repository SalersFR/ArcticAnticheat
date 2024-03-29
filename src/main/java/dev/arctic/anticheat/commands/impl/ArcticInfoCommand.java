package dev.arctic.anticheat.commands.impl;


import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.utilities.CustomUtils;
import dev.arctic.anticheat.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArcticInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(CustomUtils.translate("&bSpecify a player to get information."));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(CustomUtils.translate("&bThat player is offline or doesn't exist."));
                return true;
            }

            PlayerData targetData = Arctic.getInstance().getPlayerDataManager().getPlayerData(player);
            if (targetData == null) {
                player.sendMessage(CustomUtils.translate("&bWe're having trouble grabbing that player's data. Try again later."));
                return true;
            }


            player.sendMessage(CustomUtils.translate("&7&m--------&r &bInformation &7&m--------&r"));
            player.sendMessage(CustomUtils.translate("&r &7» Name: &b" + target.getName()));
            player.sendMessage(CustomUtils.translate("&r &7» Ping"));
            player.sendMessage(CustomUtils.translate("&r &r &7»» NMS: &b" + PlayerUtils.getPing(targetData)));
            player.sendMessage(CustomUtils.translate("&7&m---------------------------&r"));


            return true;
        }
        return true;
    }
}
