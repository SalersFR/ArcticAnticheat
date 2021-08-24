package polar.ac.listener.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import polar.ac.Polar;
import polar.ac.data.PlayerData;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Polar.INSTANCE.getDataManager().add(event.getPlayer());

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
