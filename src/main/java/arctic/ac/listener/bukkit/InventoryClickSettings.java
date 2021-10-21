package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.gui.ChecksGUI;
import arctic.ac.gui.SettingsGUI;
import arctic.ac.gui.combat.CombatChecksGUI;
import arctic.ac.gui.combat.impl.*;
import arctic.ac.gui.movement.MovementGUI;
import arctic.ac.utils.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
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
        String movementName = "Movement Checks";
        String playerName = "Player Checks";

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
                MovementGUI gui = new MovementGUI().createNewGUI();
                gui.setItems();
                gui.display(player);
            } else if (event.getSlot() == 15) {
                player.closeInventory();

                // OPEN PLAYER INVENTORY
            }
        } else if (invName.equalsIgnoreCase(combatName)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();

            final AimGUI aimGUI = new AimGUI().createNewGUI();
            final AutoclickerGUI clickerGUI = new AutoclickerGUI().createNewGUI();
            final KillAuraGUI auraGUI = new KillAuraGUI().createNewGUI();
            final ReachGUI reachGUI = new ReachGUI().createNewGUI();
            final VelocityGUI velocityGUI = new VelocityGUI().createNewGUI();

            player.updateInventory();

            switch (meta.getDisplayName()) {
                case "§bAim Checks":
                    aimGUI.setItems(player);
                    aimGUI.display(player);
                    break;
                case "§bAutoclicker Checks":
                    clickerGUI.setItems(player);
                    clickerGUI.display(player);
                    break;
                case "§bKillAura Checks":
                    auraGUI.setItems(player);
                    auraGUI.display(player);
                    break;
                case "§bReach Checks":
                    reachGUI.setItems(player);
                    reachGUI.display(player);
                    break;
                case "§bVelocity Checks":
                    velocityGUI.setItems(player);
                    velocityGUI.display(player);
                    break;
            }
        } else if (invName.equalsIgnoreCase(movementName)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();

            player.updateInventory();

            switch (meta.getDisplayName()) {

            }
        }

        // &r &7» Enabled: &b✓
        // &r &7» Enabled: &b✗

        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;
        if (meta.getLore() == null) return;
        if (meta.getLore().isEmpty()) return;

        FileConfiguration config = Arctic.INSTANCE.getConfig();

        String name = CustomUtils.strip(meta.getDisplayName().substring(0, meta.getDisplayName().length() - 1)).toLowerCase();
        String type = CustomUtils.strip(meta.getDisplayName().substring(meta.getDisplayName().length() - 1)).toLowerCase();

        if (meta.getLore().size() < 2) return;

        String punishStr = meta.getLore().get(2);
        String enableStr = meta.getLore().get(1);

        if (event.getClick().equals(ClickType.RIGHT)) {
            if (CustomUtils.strip(punishStr).contains("✓")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type + ".punish", false);
            } else if (CustomUtils.strip(punishStr).contains("✗")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type + ".punish", true);
            }
        } else if (event.getClick().equals(ClickType.LEFT)) {
            if (CustomUtils.strip(enableStr).contains("✓")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type + ".enabled", false);
            } else if (CustomUtils.strip(enableStr).contains("✗")) {
                event.setCancelled(true);
                config.set("checks.combat." + name + "." + type + ".enabled", true);
            }
        }

        Arctic.INSTANCE.saveConfig();
        Arctic.INSTANCE.reloadConfig();

        switch (CustomUtils.strip(inventory.getTitle())) {
            case "Aim Checks":
                AimGUI aim = new AimGUI().createNewGUI();
                aim.setItems((Player) event.getWhoClicked());
                aim.display((Player) event.getWhoClicked());
                break;
            case "Autoclicker Checks":
                AutoclickerGUI clicker = new AutoclickerGUI().createNewGUI();
                clicker.setItems((Player) event.getWhoClicked());
                clicker.display((Player) event.getWhoClicked());
                break;
            case "KillAura Checks":
                KillAuraGUI aura = new KillAuraGUI().createNewGUI();
                aura.setItems((Player) event.getWhoClicked());
                aura.display((Player) event.getWhoClicked());
                break;
            case "Reach Checks":
                ReachGUI reach = new ReachGUI().createNewGUI();
                reach.setItems((Player) event.getWhoClicked());
                reach.display((Player) event.getWhoClicked());
                break;
            case "Velocity Checks":
                VelocityGUI velocity = new VelocityGUI().createNewGUI();
                velocity.setItems((Player) event.getWhoClicked());
                velocity.display((Player) event.getWhoClicked());
                break;
        }
    }
}
