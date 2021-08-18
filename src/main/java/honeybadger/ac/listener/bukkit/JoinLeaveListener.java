package honeybadger.ac.listener.bukkit;

import honeybadger.ac.HoneyBadger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        HoneyBadger.INSTANCE.getDataManager().add(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        HoneyBadger.INSTANCE.getDataManager().remove(event.getPlayer());
    }
}
