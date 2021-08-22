package honeybadger.ac.listener.bukkit;

import honeybadger.ac.utils.CustomUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickSettings implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getTitle() == null) return;
        
        String invName = CustomUtils.strip(event.getClickedInventory().getTitle());
        String settingsName = "HoneyBadger";

        if (invName.equalsIgnoreCase(settingsName)) {
            event.setCancelled(true);
        }
    }
}
