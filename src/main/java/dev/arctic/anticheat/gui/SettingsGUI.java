package dev.arctic.anticheat.gui;


import dev.arctic.anticheat.utilities.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsGUI {

    Inventory inventory;

    public SettingsGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&',
                "&b&lArctic"));

        this.inventory = inv;

        return this;
    }

    public void setItems() {
        inventory.setItem(22, CustomUtils.createItem(Material.COMPASS, "&bChecks", "&r ", "&7 - &7&oEnabled/Disable checks"));
    }

    public void display(Player p) {
        p.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
