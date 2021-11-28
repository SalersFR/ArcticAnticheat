package arctic.ac.gui.player.impl;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.utils.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ScaffoldGUI {

    private Inventory inventory;

    public ScaffoldGUI createNewGUI() {
        Inventory inv = Bukkit.createInventory(null, 36, "§b§lScaffold Checks");


        this.inventory = inv;
        return this;
    }

    public void setItems(Player player) {
        PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(player);
        if (data == null) {
            player.closeInventory();
            return;
        }

        String name = "scaffold";

        List<Check> checks = data.getChecks();
        List<Check> aims = new ArrayList<>();


        for (Check c : checks) {
            if (c.getConfigName().contains(name)) {
                aims.add(c);
            }
        }

        for (Check c : aims) {


            boolean enabled = Arctic.INSTANCE.getConfig().getBoolean("checks.player." + name + "." + c.getType().toLowerCase() + ".enabled");
            boolean punishable = Arctic.INSTANCE.getConfig().getBoolean("checks.player." + name + "." + c.getType().toLowerCase() + ".punish");

            inventory.setItem(aims.indexOf(c), CustomUtils.createItem((enabled ? Material.MAP : Material.EMPTY_MAP),
                    "&b" + c.getName() + c.getType(),
                    "&r &7» Description: &b" + c.getDesc(),
                    "&r &7» Enabled: &b" + (enabled ? "✓" : "✗"),
                    "&r &7» Punish: &b" + (punishable ? "✓" : "✗"),
                    "&r &7» Max VL: &b" + c.getBanVL(),
                    "",
                    "&7&oLeft click to toggle enabled status.",
                    "&7&oRight click to toggle punish status."));
        }
    }

    public void display(final Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
