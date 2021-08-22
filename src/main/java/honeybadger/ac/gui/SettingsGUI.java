package honeybadger.ac.gui;

import honeybadger.ac.utils.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsGUI {

    Inventory inventory;

    public SettingsGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&',
                "&c&lHoneyBadger"));

        this.inventory = inv;

        return this;
    }

    public void setItems() {
        inventory.setItem(11, CustomUtils.createItem(Material.COMPASS, "&cChecks", "&r ", "&7 - &7&oEnabled/Disable checks"));
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
