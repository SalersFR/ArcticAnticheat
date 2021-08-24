package polar.ac.listener.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import polar.ac.utils.CustomUtils;

public class InventoryClickSettings implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getTitle() == null) return;

        String invName = CustomUtils.strip(event.getClickedInventory().getTitle());
        String settingsName = "Polar";

        if (invName.equalsIgnoreCase(settingsName)) {
            event.setCancelled(true);
        }
    }
}
