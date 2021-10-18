package arctic.ac.gui.combat.impl;

import arctic.ac.Arctic;
import arctic.ac.gui.combat.CombatChecksGUI;
import arctic.ac.utils.CustomUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class Aim {

    private Inventory inventory;

    public Aim createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, CustomUtils.translate("&b&lCombat Checks"));

        this.inventory = inv;
        return this;
    }

    public void setItems() {
        ArrayList<String> checks = new ArrayList<>();
        HashMap<String, Boolean> checkAndActive = new HashMap<>();
        for(String aimChecks : Arctic.INSTANCE.getConfig().getConfigurationSection("checks.combat.aim").getKeys(false)) {
             
        }

        for (String s : checkAndActive.keySet()) {
            String var1 = s.substring(s.length() - 1);
            var1 = var1.toUpperCase();
            inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                    "&b" + StringUtils.capitalize(s.substring(0, s.length() - 1) + var1)), CustomUtils.translate("&r &7» Enabled: " + (checkAndActive.get(s) ? "&a✓" : "&c✗"))));
        }
    }
}
