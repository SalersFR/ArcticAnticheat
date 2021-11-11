package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.util.Vector;

public class KillAuraI extends Check {

    public KillAuraI(PlayerData data) {
        super(data, "KillAura", "I", "combat.killaura.I", "Checks for failrate",true);
    }
    public boolean sentAttack;
    public long timeDiff;

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            UseEntityEvent attack = (UseEntityEvent) e;
            if (!attack.getAction().equals(EnumWrappers.EntityUseAction.ATTACK)) return;

            sentAttack = true;
            timeDiff = System.currentTimeMillis();
        }
        if (e instanceof ArmAnimationEvent) {
            //if (sentAttack) data.getPlayer().sendMessage("timediff " + (System.currentTimeMillis() - timeDiff));
            sentAttack = false;

            Vector attacker = data.getBukkitPlayerFromUUID().getEyeLocation().toVector();
            Vector target = (data.getPastEntityLocations().get((((CraftPlayer) data.getPlayer()).getHandle().ping / 50)).toVector());
            double diff = yawDiff(attacker.toLocation(((CraftPlayer) data.getPlayer()).getWorld()),target.toLocation(((CraftPlayer) data.getPlayer()).getWorld()));
        }
    }

    public static double yawDiff(Location player, Location target) {
        Location clonedFrom = player.clone();
        Vector startVector = clonedFrom.toVector();
        Vector targetVector = target.toVector();
        clonedFrom.setDirection(targetVector.subtract(startVector));
        return clonedFrom.getYaw();
    }
}
