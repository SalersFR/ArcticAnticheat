package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Arctic.INSTANCE.getDataManager().add(event.getPlayer());

        if (event.getPlayer().hasPermission("alerts.see") && event.getPlayer().hasPermission("alerts.command")) {
            if (event.getPlayer().hasMetadata("ALERTS_ON_NORMAL") || event.getPlayer().hasMetadata("ALERTS_ON_VERBOSE"))
                return;

            event.getPlayer().setMetadata("ALERTS_ON_NORMAL", new FixedMetadataValue(Arctic.INSTANCE, true));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if (Arctic.INSTANCE.isCitizensPresent()) {
            data.getInteractionData().getEntityANPC().destroy();
        }
        Arctic.INSTANCE.getDataManager().remove(event.getPlayer());
    }
}