package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.gui.ChecksGUI;
import arctic.ac.gui.CombatChecksGUI;
import arctic.ac.utils.CustomUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryClickSettings implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;
        if (event.getInventory() == null) return;
        if (event.getInventory().getTitle() == null) return;
        if (event.getClickedInventory().getTitle() == null) return;
        if (event.getCurrentItem() == null) return;

        String invName = CustomUtils.strip(event.getClickedInventory().getTitle());
        String settingsName = "Arctic";
        String checksName = "Checks";
        String combatName = "Combat Checks";

        if (invName.equalsIgnoreCase(settingsName)) {
            event.setCancelled(true);

            if (event.getSlot() == 11) {
                player.closeInventory();

                ChecksGUI gui = new ChecksGUI().createNewGUI();
                gui.setItems();
                gui.display(player);
            }
        } else if (invName.equalsIgnoreCase(checksName)) {
            event.setCancelled(true);

            if (event.getSlot() == 11) {
                player.closeInventory();

                CombatChecksGUI gui = new CombatChecksGUI().createNewGUI();
                gui.setItems();
                gui.display(player);
            } else if (event.getSlot() == 13) {
                player.closeInventory();

                // OPEN MOVEMENT INVENTORY
            } else if (event.getSlot() == 15) {
                player.closeInventory();

                // OPEN PLAYER INVENTORY
            }
        } else if (invName.equalsIgnoreCase(combatName)) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            List<String> newLore = new ArrayList<>();
            // ✓
            // ✗
            // &r &7» Enabled: " + (checkAndActive.get(s) ? "&a✓" : "&c✗"))

            for (String s : meta.getLore()) {
                if (CustomUtils.strip(s).contains("✓")) {
                    newLore.add(CustomUtils.translate("&r &7» Enabled: &c✗"));
                    Arctic.INSTANCE.getConfig().set("checks.combat." + CustomUtils.strip(meta.getDisplayName().substring(0, meta.getDisplayName().length() - 1)
                    ).toLowerCase() + "." + CustomUtils.strip(meta.getDisplayName().substring(meta.getDisplayName().length() - 1)).toLowerCase() + ".enabled", false);
                    Arctic.INSTANCE.saveConfig();

                    meta.getLore().clear();
                    meta.setLore(newLore);

                    Arctic.INSTANCE.reloadConfig();
                } else if (CustomUtils.strip(s).contains("✗")) {
                    newLore.add(CustomUtils.translate("&r &7» Enabled: &a✓"));
                    Arctic.INSTANCE.getConfig().set("checks.combat." + CustomUtils.strip(meta.getDisplayName().substring(0, meta.getDisplayName().length() - 1)
                    ).toLowerCase() + "." + CustomUtils.strip(meta.getDisplayName().substring(meta.getDisplayName().length() - 1)).toLowerCase() + ".enabled", true);
                    Arctic.INSTANCE.saveConfig();

                    meta.getLore().clear();
                    meta.setLore(newLore);
                }
            }
            item.setItemMeta(meta);

            event.getInventory().setItem(event.getSlot(), item);

            player.updateInventory();
        }
    }
}
