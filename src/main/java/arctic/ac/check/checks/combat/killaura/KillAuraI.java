package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ALocation;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

import org.bukkit.Location;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class KillAuraI extends Check {

    public boolean sentAttack;
    public long timeDiff;
    public double diff;
    public double dist;
    public double buffer;

    public KillAuraI(PlayerData data) {
        super(data, "KillAura", "I", "combat.killaura.i", "Checks for failrate", true);
    }

    public static double yawDiff(Location player, Location target) {
        Location clonedFrom = player.clone();
        Vector startVector = clonedFrom.toVector();
        Vector targetVector = target.toVector();
        clonedFrom.setDirection(targetVector.subtract(startVector));
        return clonedFrom.getYaw();
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            UseEntityEvent attack = (UseEntityEvent) e;
            if (!attack.getAction().equals(WrappedPacketInUseEntity.EntityUseAction.ATTACK)) return;

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


            final List<ALocation> pastVectors = ray(data.getNetworkProcessor().getKeepAlivePing());
            double diff2 = pastVectors.stream().mapToDouble(target -> yawDiff(data.getPlayer().getLocation(),
                    target.toVector().toLocation( data.getPlayer().getWorld()))).min().orElse(0);
            this.diff = diff2;
            this.dist = pastVectors.stream().mapToDouble(target -> target.toVector().distance(attacker)).min().orElse(0);
        }
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
