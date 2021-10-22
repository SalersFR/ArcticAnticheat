package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.gui.ChecksGUI;
import arctic.ac.gui.combat.CombatChecksGUI;
import arctic.ac.gui.combat.impl.*;
import arctic.ac.gui.movement.MovementGUI;
import arctic.ac.gui.movement.impl.*;
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

        final AimGUI aimGUI = new AimGUI().createNewGUI();
        final AutoclickerGUI clickerGUI = new AutoclickerGUI().createNewGUI();
        final KillAuraGUI auraGUI = new KillAuraGUI().createNewGUI();
        final ReachGUI reachGUI = new ReachGUI().createNewGUI();
        final VelocityGUI velocityGUI = new VelocityGUI().createNewGUI();
        final FlyGUI flyGUI = new FlyGUI().createNewGUI();
        final MotionGUI motionGUI = new MotionGUI().createNewGUI();
        final NoFallGUI nofallGUI = new NoFallGUI().createNewGUI();
        final SpeedGUI speedGUI = new SpeedGUI().createNewGUI();
        final StepGUI stepGUI = new StepGUI().createNewGUI();

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
                case "§bFly Checks":
                    flyGUI.setItems(player);
                    flyGUI.display(player);
                    break;
                case "§bMotion Checks":
                    motionGUI.setItems(player);
                    motionGUI.display(player);
                    break;
                case "§bNoFall Checks":
                    nofallGUI.setItems(player);
                    nofallGUI.display(player);
                    break;
                case "§bSpeed Checks":
                    speedGUI.setItems(player);
                    speedGUI.display(player);
                    break;
                case "§bStep Checks":
                    stepGUI.setItems(player);
                    stepGUI.display(player);
                    break;
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
        if (meta.getLore().get(2) == null) return;

        String punishStr = meta.getLore().get(2);
        String enableStr = meta.getLore().get(1);


        if (event.getClick().equals(ClickType.RIGHT)) {
            if (CustomUtils.strip(punishStr).contains("✓")) {
                event.setCancelled(true);
                if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.COMBAT) {
                    config.set("checks.combat." + name + "." + type + ".punish", false);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.MOVEMENT) {
                    config.set("checks.movement." + name + "." + type + ".punish", false);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.PLAYER) {
                    config.set("checks.player." + name + "." + type + ".punish", false);
                } else {
                    Bukkit.broadcastMessage(CustomUtils.strip(invName).toLowerCase().split(" ")[0]);
                }
            } else if (CustomUtils.strip(punishStr).contains("✗")) {
                event.setCancelled(true);
                if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.COMBAT) {
                    config.set("checks.combat." + name + "." + type + ".punish", true);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.MOVEMENT) {
                    config.set("checks.movement." + name + "." + type + ".punish", true);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.PLAYER) {
                    config.set("checks.player." + name + "." + type + ".punish", true);
                } else {
                    Bukkit.broadcastMessage(CustomUtils.strip(invName).toLowerCase().split(" ")[0]);
                }
            }
        } else if (event.getClick().equals(ClickType.LEFT)) {
            if (CustomUtils.strip(enableStr).contains("✓")) {
                event.setCancelled(true);
                if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.COMBAT) {
                    config.set("checks.combat." + name + "." + type + ".enabled", false);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.MOVEMENT) {
                    config.set("checks.movement." + name + "." + type + ".enabled", false);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.PLAYER) {
                    config.set("checks.player." + name + "." + type + ".enabled", false);
                } else {
                    Bukkit.broadcastMessage(CustomUtils.strip(invName).toLowerCase().split(" ")[0]);
                }
            } else if (CustomUtils.strip(enableStr).contains("✗")) {
                event.setCancelled(true);
                if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.COMBAT) {
                    config.set("checks.combat." + name + "." + type + ".enabled", true);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.MOVEMENT) {
                    config.set("checks.movement." + name + "." + type + ".enabled", true);
                } else if (CustomUtils.getCategory(CustomUtils.strip(invName).toLowerCase().split(" ")[0]) == CustomUtils.Category.PLAYER) {
                    config.set("checks.player." + name + "." + type + ".enabled", true);
                } else {
                    Bukkit.broadcastMessage(CustomUtils.strip(invName).toLowerCase().split(" ")[0]);
                }
            }
        }

        Arctic.INSTANCE.saveConfig();
        Arctic.INSTANCE.reloadConfig();

        switch (CustomUtils.strip(inventory.getTitle())) {
            case "Aim Checks":
                AimGUI aim = new AimGUI().createNewGUI();
                aim.setItems(player);
                aim.display(player);
                break;
            case "Autoclicker Checks":
                AutoclickerGUI clicker = new AutoclickerGUI().createNewGUI();
                clicker.setItems(player);
                clicker.display(player);
                break;
            case "KillAura Checks":
                KillAuraGUI aura = new KillAuraGUI().createNewGUI();
                aura.setItems(player);
                aura.display(player);
                break;
            case "Reach Checks":
                ReachGUI reach = new ReachGUI().createNewGUI();
                reach.setItems(player);
                reach.display(player);
                break;
            case "Velocity Checks":
                VelocityGUI velocity = new VelocityGUI().createNewGUI();
                velocity.setItems(player);
                velocity.display(player);
                break;
            case "Fly Checks":
                flyGUI.setItems(player);
                flyGUI.display(player);
                break;
            case "Motion Checks":
                motionGUI.setItems(player);
                motionGUI.display(player);
                break;
            case "NoFall Checks":
                nofallGUI.setItems(player);
                nofallGUI.display(player);
                break;
            case "Speed Checks":
                speedGUI.setItems(player);
                speedGUI.display(player);
                break;
            case "Step Checks":
                stepGUI.setItems(player);
                stepGUI.display(player);
                break;
        }
    }
}
