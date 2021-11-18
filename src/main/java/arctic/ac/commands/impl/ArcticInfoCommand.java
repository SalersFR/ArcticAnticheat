package arctic.ac.commands.impl;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import arctic.ac.utils.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

            PlayerData targetData = Arctic.INSTANCE.getDataManager().getPlayerData(target);
            if (targetData == null) {
                player.sendMessage(CustomUtils.translate("&bWe're having trouble grabbing that player's data. Try again later."));
                return true;
            }

            int verCard = targetData.getNetworkProcessor().getClientVersion();
            String version = (verCard <= 5 ? "1.7" : verCard <= 47 ? "1.8" : verCard <= 110 ? "1.9" : verCard <= 210 ? "1.10" : "> 1.11");

            player.sendMessage(CustomUtils.translate("&7&m--------&r &bInformation &7&m--------&r"));
            player.sendMessage(CustomUtils.translate("&r &7» Name: &b" + target.getName()));
            player.sendMessage(CustomUtils.translate("&r &7» Ping"));
            player.sendMessage(CustomUtils.translate("&r &r &7»» NMS: &b" + ((CraftPlayer) player).getHandle().ping));
            player.sendMessage(CustomUtils.translate("&r &r &7»» Transaction: &b" + targetData.getNetworkProcessor().getTransactionPing()));
            player.sendMessage(CustomUtils.translate("&r &r &7»» Keep Alive: &b" + targetData.getNetworkProcessor().getKeepAlivePing()));
            player.sendMessage(CustomUtils.translate("&r &7» Client: &b" + targetData.getNetworkProcessor().getClientBrand())); // TODO: Get Client
            player.sendMessage(CustomUtils.translate("&r &7» Version: &b" + version + " (" + verCard + ")")); // TODO: Get version
            player.sendMessage(CustomUtils.translate("&7&m---------------------------&r"));


            return true;
        }
        return true;
    }
}
