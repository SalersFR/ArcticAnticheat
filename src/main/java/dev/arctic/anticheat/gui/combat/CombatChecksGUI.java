package dev.arctic.anticheat.gui.combat;

import dev.arctic.anticheat.utilities.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CombatChecksGUI {

    Inventory inventory;

    public CombatChecksGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, CustomUtils.translate("&b&lCombat Checks"));

        this.inventory = inv;
        return this;
    }

    public void setItems() {
        final ItemStack aim = new ItemStack(Material.ARROW);
        final ItemStack autoclicker = new ItemStack(Material.STICK);
        final ItemStack killaura = new ItemStack(Material.GOLD_SWORD);
        final ItemStack reach = new ItemStack(Material.BOW);
        final ItemStack velocity = new ItemStack(Material.ANVIL);

        final ItemMeta aimMeta = aim.getItemMeta();
        final ItemMeta autoclickerMeta = autoclicker.getItemMeta();
        final ItemMeta killauraMeta = killaura.getItemMeta();
        final ItemMeta reachMeta = reach.getItemMeta();
        final ItemMeta velocityMeta = velocity.getItemMeta();

        aimMeta.setDisplayName("§bAim Checks");
        autoclickerMeta.setDisplayName("§bAutoclicker Checks");
        killauraMeta.setDisplayName("§bKillAura Checks");
        reachMeta.setDisplayName("§bReach Checks");
        velocityMeta.setDisplayName("§bVelocity Checks");

        aim.setItemMeta(aimMeta);
        autoclicker.setItemMeta(autoclickerMeta);
        killaura.setItemMeta(killauraMeta);
        reach.setItemMeta(reachMeta);
        velocity.setItemMeta(velocityMeta);

        this.inventory.setItem(10, aim);
        this.inventory.setItem(11, autoclicker);
        this.inventory.setItem(13, killaura);
        this.inventory.setItem(15, reach);
        this.inventory.setItem(16, velocity);

    }

    public void display(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
