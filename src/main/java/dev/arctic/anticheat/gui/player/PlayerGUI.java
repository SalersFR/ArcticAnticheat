package dev.arctic.anticheat.gui.player;


import dev.arctic.anticheat.utilities.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerGUI {

    private Inventory inventory;

    public PlayerGUI createNewGUI() {
        final Inventory inv = Bukkit.createInventory(null, 27, CustomUtils.translate("&b&lPlayer Checks"));


        this.inventory = inv;
        return this;
    }

    public void setItems() {
        final ItemStack badItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
        final ItemStack timerItem = new ItemStack(Material.WATCH);
        final ItemStack scaffoldItem = new ItemStack(Material.SANDSTONE);
        final ItemStack groundItem = new ItemStack(Material.SLIME_BLOCK);

        final ItemMeta badMeta = badItem.getItemMeta();
        final ItemMeta timerMeta = timerItem.getItemMeta();
        final ItemMeta scaffoldMeta = scaffoldItem.getItemMeta();
        final ItemMeta groundMeta = groundItem.getItemMeta();

        badMeta.setDisplayName("§bBadPackets Checks");
        timerMeta.setDisplayName("§bTimer Checks");
        scaffoldMeta.setDisplayName("§bScaffold Checks");
        groundMeta.setDisplayName("§bGround Checks");

        badItem.setItemMeta(badMeta);
        timerItem.setItemMeta(timerMeta);
        scaffoldItem.setItemMeta(scaffoldMeta);
        groundItem.setItemMeta(groundMeta);

        inventory.setItem(10, badItem);
        inventory.setItem(12, timerItem);
        inventory.setItem(14, scaffoldItem);
        inventory.setItem(16, groundItem);


    }

    public void display(final Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
