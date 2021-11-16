package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ALocation;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class KillAuraI extends Check {

    public KillAuraI(PlayerData data) {
        super(data, "KillAura", "I", "combat.killaura.i", "Checks for failrate", true);
    }

    public boolean sentAttack;
    public long timeDiff;
    public double diff;
    public double dist;
    public double buffer;

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            UseEntityEvent attack = (UseEntityEvent) e;
            if (!attack.getAction().equals(EnumWrappers.EntityUseAction.ATTACK)) return;

            sentAttack = false;
        }
        if (e instanceof ArmAnimationEvent) {
            //if (sentAttack) data.getPlayer().sendMessage("timediff " + (System.currentTimeMillis() - timeDiff));
            if (sentAttack && (System.currentTimeMillis() - timeDiff) > 50) {
                if (dist < 2.5) {
                    if (diff < 100 && diff != 0) {
                        buffer++;
                        if (buffer > 3) {
                            fail("buffer " + buffer + " diff " + diff + " reach " + dist);
                        }
                    } else if (buffer > 0) buffer -= 0.05;
                } else if (buffer > 0) buffer -= 0.05;
            }

            sentAttack = true;
            timeDiff = System.currentTimeMillis();


            Vector attacker = data.getBukkitPlayerFromUUID().getEyeLocation().toVector();
            final EntityPlayer nms = ((CraftPlayer) data.getPlayer()).getHandle();

            final List<ALocation> pastVectors = ray(nms.ping);
            double diff2 = pastVectors.stream().mapToDouble(target -> yawDiff(attacker.toLocation(((CraftPlayer) data.getPlayer()).getWorld()), target.toVector().toLocation(((CraftPlayer) data.getPlayer()).getWorld()))).min().orElse(0);
            this.diff = diff2;
            this.dist = pastVectors.stream().mapToDouble(target -> target.toVector().distance(attacker)).min().orElse(0);
        }
    }

    public static double yawDiff(Location player, Location target) {
        Location clonedFrom = player.clone();
        Vector startVector = clonedFrom.toVector();
        Vector targetVector = target.toVector();
        clonedFrom.setDirection(targetVector.subtract(startVector));
        return clonedFrom.getYaw();
    }

    private List<ALocation> ray(final int ping) {

        final List<ALocation> toReturn = new ArrayList<>();

        try {
            for (int i = 0; i < 2; i++) {
                toReturn.add(data.getPastEntityLocations().get(i + (ping / 50)));
            }
        } catch (IndexOutOfBoundsException e) {

        }

        return toReturn;
    }
}
