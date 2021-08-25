package polar.ac.listener.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import polar.ac.Polar;
import polar.ac.data.PlayerData;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Polar.INSTANCE.getDataManager().add(event.getPlayer());

        if (event.getPlayer().hasPermission("alerts.see") && event.getPlayer().hasPermission("alerts.command")) {
            if (event.getPlayer().hasMetadata("ALERTS_ON_NORMAL") || event.getPlayer().hasMetadata("ALERTS_ON_VERBOSE")) return;

            event.getPlayer().setMetadata("ALERTS_ON_NORMAL", new FixedMetadataValue(Polar.INSTANCE, true));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final PlayerData data = Polar.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if (Polar.INSTANCE.isCitizensPresent()){
            data.getInteractionData().getEntityANPC().destroy();
        }
        Polar.INSTANCE.getDataManager().remove(event.getPlayer());
    }
}
