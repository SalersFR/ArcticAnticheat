package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.gui.ChecksGUI;
import arctic.ac.gui.combat.CombatChecksGUI;
import arctic.ac.gui.combat.impl.*;
import arctic.ac.utils.CustomUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
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

        Inventory inventory = event.getClickedInventory();

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

            final AimGUI aimGUI = new AimGUI().createNewGUI();
            final AutoclickerGUI clickerGUI = new AutoclickerGUI().createNewGUI();
            final KillAuraGUI auraGUI = new KillAuraGUI().createNewGUI();
            final ReachGUI reachGUI = new ReachGUI().createNewGUI();
            final VelocityGUI velocityGUI = new VelocityGUI().createNewGUI();

            // ✓
            // ✗
            // &r &7» Enabled: " + (checkAndActive.get(s) ? "&a✓" : "&c✗"))


            player.updateInventory();

            switch (meta.getDisplayName()) {
                case "§b§lAim Checks":
                    aimGUI.setItems(player);
                    aimGUI.display(player);
                    break;
                case "§b§lAutoclicker Checks":
                    clickerGUI.setItems();
                    clickerGUI.display(player);
                    break;
                case "§b§lKillAura Checks":
                    auraGUI.setItems();
                    auraGUI.display(player);
                    break;
                case "§b§lReach Checks":
                    reachGUI.setItems();
                    reachGUI.display(player);
                    break;
                case "§b§lVelocity Checks":
                    velocityGUI.setItems();
                    velocityGUI.display(player);
                    break;
            }
        }

        // &r &7» Enabled: &b✓
        // &r &7» Enabled: &b✗

        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        List<String> newLore = new ArrayList<>();

        if (meta.getLore() == null) return;
        if (meta.getLore().isEmpty()) return;

        FileConfiguration config = Arctic.INSTANCE.getConfig();

        for (String s : meta.getLore()) {

            String name = CustomUtils.strip(meta.getDisplayName().substring(0, meta.getDisplayName().length() - 1)).toLowerCase();
            String type = CustomUtils.strip(meta.getDisplayName().substring(meta.getDisplayName().length() - 1)).toLowerCase();

            boolean enabled = config.getBoolean("checks.combat." + name + "." + type + ".enabled");
            boolean punishable = config.getBoolean("checks.combat." + name + "." + type + ".punish");
            newLore.clear();
            if (CustomUtils.strip(s).contains("✓")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type+ ".enabled", false);
                Arctic.INSTANCE.saveConfig();
                Arctic.INSTANCE.reloadConfig();

                newLore.addAll(meta.getLore());
                newLore.set(1, CustomUtils.translate("&r &7» Enabled: &b" + (enabled ? "✓" : "✗")));
                newLore.set(2, CustomUtils.translate("&r &7» Punish: &b" + (punishable ? "✓" : "✗")));

                meta.setLore(newLore);
                item.setItemMeta(meta);
                inventory.setItem(event.getSlot(), item);
                player.updateInventory();
            }  else if (CustomUtils.strip(s).contains("✗")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type+ ".enabled", true);
                Arctic.INSTANCE.saveConfig();
                Arctic.INSTANCE.reloadConfig();


                newLore.addAll(meta.getLore());
                newLore.set(1, CustomUtils.translate("&r &7» Enabled: &b" + (enabled ? "✓" : "✗")));
                newLore.set(2, CustomUtils.translate("&r &7» Punish: &b" + (punishable ? "✓" : "✗")));

                meta.setLore(newLore);
                item.setItemMeta(meta);
                inventory.setItem(event.getSlot(), item);
                player.updateInventory();
            }




        }
    }
}
