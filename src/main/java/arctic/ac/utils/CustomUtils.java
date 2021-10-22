package arctic.ac.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CustomUtils {


    public float getFriction(Location loc) {
        try {
            String block = loc.add(0, -1, 0).getBlock().getType().name().toLowerCase();
            return 0.91f * (block.equals("blue_ice") ? 0.989f : block.contains("ice") ? 0.98f : block.contains("slime") ? 0.8f : 0.6f);
        } catch (Exception ignored) {
            return 0.91f * 0.6f;
        }
    }

    public String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String strip(String s) {
        return ChatColor.stripColor(s);
    }

    public ItemStack createItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(translate(displayName));
        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            newLore.add(translate(s));
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(translate(displayName));
        item.setItemMeta(meta);

        return item;
    }

    public void consoleLog(String s) {
        Bukkit.getServer().getConsoleSender().sendMessage(translate(s));
    }

    public Category getCategory(String checkName) {
        switch (checkName) {
            case "aim":
            case "autoclicker":
            case "killaura":
            case "reach":
            case "velocity":
                return Category.COMBAT;
            case "fly":
            case "motion":
            case "nofall":
            case "speed":
            case "step":
                return Category.MOVEMENT;
            case "badpackets":
            case "scaffold":
            case "timer":
                return Category.PLAYER;
        }

        return null;
    }

    public enum Category {
        MOVEMENT, COMBAT, PLAYER;
    }
}
