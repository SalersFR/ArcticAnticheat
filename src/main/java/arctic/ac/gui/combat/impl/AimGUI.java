package arctic.ac.gui.combat.impl;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.utils.CustomUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class AimGUI {

    private Inventory inventory;

    public AimGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 27, "§b§lAim Checks");


        this.inventory = inv;
        return this;
    }

    public void setItems(Player player) {
        ArrayList<String> checks = new ArrayList<>();
        HashMap<String, Boolean> checkAndActive = new HashMap<>();
        PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(player);
        if (data == null) {
            player.closeInventory();
            return;
        }

        for (String checkVariation : Arctic.INSTANCE.getConfig().getConfigurationSection("checks.combat" + "." + "aim").getKeys(false)) {
            checks.add("aim" + checkVariation);
            checkAndActive.put("aim" + checkVariation, Arctic.INSTANCE.getConfig().getBoolean("checks.combat." + "aim" + "." + checkVariation + ".enabled"));

        }

        for (String s : checkAndActive.keySet()) {
            String var1 = s.substring(s.length() - 1);
            var1 = var1.toUpperCase();

            final boolean enabled = checkAndActive.get(s);

            if (checks.indexOf(s) > 27) return;

            Check c = null;

            for (Check check : data.getChecks()) {
                if (check.getConfigName().equalsIgnoreCase(StringUtils.capitalize(s.substring(0, s.length() - 1) + var1))) {
                    c = check;
                }
            }

            if (enabled)
                inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                                "&b" + StringUtils.capitalize(s.substring(0, s.length() - 1) + var1)),
                        CustomUtils.translate("&r &7» Description: &b" + (c == null ? "" : c.getDesc())), CustomUtils.translate("&r &7» Enabled: &b✓")));

            else
                inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                                "&b" + StringUtils.capitalize(s.substring(0, s.length() - 1) + var1)),
                        CustomUtils.translate("&r &7» Description: &b" + (c == null ? "" : c.getDesc())), CustomUtils.translate("&r &7» Enabled: &b✗")));
        }

        for (String s : checkAndActive.keySet()) {
            String var1 = s.substring(s.length() - 1);
            var1 = var1.toUpperCase();

            final boolean enabled = checkAndActive.get(s);

            Check c = null;

            for (Check check : data.getChecks()) {
                if (check.getConfigName().equalsIgnoreCase(StringUtils.capitalize(s.substring(0, s.length() - 1) + var1))) {
                    c = check;
                }
            }

            if (enabled)
                inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                                "&b" + StringUtils.capitalize(s.substring(0, s.length() - 1) + var1)),
                        CustomUtils.translate("&r &7» Description: &b" + (c == null ? "" : c.getDesc())), CustomUtils.translate("&r &7» Enabled: &b✓")));

            else
                inventory.setItem(checks.indexOf(s), CustomUtils.createItem(Material.PAPER, CustomUtils.translate("" +
                                "&b" + StringUtils.capitalize(s.substring(0, s.length() - 1) + var1)),
                        CustomUtils.translate("&r &7» Description: &b" + (c == null ? "" : c.getDesc())), CustomUtils.translate("&r &7» Enabled: &b✗")));
        }
    }

    public void display(Player player) {
        player.openInventory(inventory);
    }
}
