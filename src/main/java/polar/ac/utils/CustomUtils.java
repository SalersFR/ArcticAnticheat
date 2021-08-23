package polar.ac.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CustomUtils {

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
}