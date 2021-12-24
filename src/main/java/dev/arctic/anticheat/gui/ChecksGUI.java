package dev.arctic.anticheat.gui;


import dev.arctic.anticheat.utilities.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ChecksGUI {

    Inventory mainChecksGUI;

    public ChecksGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&',
                "&b&lChecks"));

        this.mainChecksGUI = inv;
        return this;
    }

    public void setItems() {

        mainChecksGUI.setItem(11, CustomUtils.createItem(Material.IRON_SWORD, CustomUtils.translate("" +
                        "&bCombat"), CustomUtils.translate("&r "), CustomUtils.translate("&7&oChecks include (not limited to):"),
                CustomUtils.translate("&7&o - Autoclicker - Reach - KillAura")));

        mainChecksGUI.setItem(13, CustomUtils.createItem(Material.FEATHER, CustomUtils.translate("" +
                        "&bMovement"), CustomUtils.translate("&r "), CustomUtils.translate("&7&oChecks include (not limited to):"),
                CustomUtils.translate("&7&o - Fly - Motion - Speed")));

        mainChecksGUI.setItem(15, CustomUtils.createItem(Material.SKULL_ITEM, CustomUtils.translate("" +
                        "&bPlayer"), CustomUtils.translate("&r "), CustomUtils.translate("&7&oChecks include (not limited to):"),
                CustomUtils.translate("&7&o - Scaffold - Timer - BadPackets")));

    }

    public void display(Player player) {
        player.openInventory(mainChecksGUI);
    }

    public Inventory getInventory() {
        return mainChecksGUI;
    }
}
