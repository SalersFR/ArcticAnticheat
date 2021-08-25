package polar.ac.gui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import polar.ac.Polar;
import polar.ac.utils.CustomUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CombatChecksGUI {

    Inventory inventory;

    public CombatChecksGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, CustomUtils.translate("&b&lCombat Checks"));

        this.inventory = inv;
        return this;
    }

    public void setItems() {
        ArrayList<String> checks = new ArrayList<>();
        HashMap<String, Boolean> checkAndActive = new HashMap<>();
        for (String checkName : Polar.INSTANCE.getConfig().getConfigurationSection("checks." + "combat").getKeys(false)) {
            for (String checkVariation : Polar.INSTANCE.getConfig().getConfigurationSection("checks.combat" + "." + checkName).getKeys(false)) {
                checks.add(checkName + "" + checkVariation);
                checkAndActive.put(checkName + "" + checkVariation, Polar.INSTANCE.getConfig().getBoolean("checks.combat." + checkName + "." + checkVariation + ".enabled"));
            }
        }

        for (String s : checkAndActive.keySet()) {
            String var1 = s.substring(s.length()-1);
            var1 = var1.toUpperCase();
            inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                    "&b" + StringUtils.capitalize(s.substring(0, s.length()-1) + var1)), CustomUtils.translate("&r &7» Enabled: " + (checkAndActive.get(s) ? "&a✓" : "&c✗"))));
        }
    }

    public void display(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
