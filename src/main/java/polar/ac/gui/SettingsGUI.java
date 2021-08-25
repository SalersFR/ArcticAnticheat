package polar.ac.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import polar.ac.utils.CustomUtils;

public class SettingsGUI {

    Inventory inventory;

    public SettingsGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&',
                "&b&lPolar"));

        this.inventory = inv;

        return this;
    }

    public void setItems() {
        inventory.setItem(11, CustomUtils.createItem(Material.COMPASS, "&bChecks", "&r ", "&7 - &7&oEnabled/Disable checks"));
    }

    public void display(Player p) {
        p.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
