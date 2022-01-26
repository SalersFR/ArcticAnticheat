package dev.arctic.anticheat.gui.movement;


import dev.arctic.anticheat.utilities.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MovementGUI {

    Inventory inventory;

    public MovementGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, CustomUtils.translate("&b&lMovement Checks"));

        this.inventory = inv;
        return this;
    }

    public void setItems() {
        final ItemStack fly = new ItemStack(Material.FEATHER);
        final ItemStack motion = new ItemStack(Material.GOLD_PLATE);
        final ItemStack speed = new ItemStack(Material.DIAMOND_BOOTS);
        final ItemStack step = new ItemStack(Material.SMOOTH_STAIRS);

        final ItemMeta flyMeta = fly.getItemMeta();
        final ItemMeta motionMeta = motion.getItemMeta();
        final ItemMeta speedMeta = speed.getItemMeta();
        final ItemMeta stepMeta = step.getItemMeta();

        flyMeta.setDisplayName("§bFlight Checks");
        motionMeta.setDisplayName("§bMotion Checks");
        speedMeta.setDisplayName("§bSpeed Checks");
        stepMeta.setDisplayName("§bStep Checks");

        fly.setItemMeta(flyMeta);
        motion.setItemMeta(motionMeta);
        speed.setItemMeta(speedMeta);
        step.setItemMeta(stepMeta);

        this.inventory.setItem(10, fly);
        this.inventory.setItem(12, motion);
        this.inventory.setItem(14, speed);
        this.inventory.setItem(16, step);
    }

    public void display(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
