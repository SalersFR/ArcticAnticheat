package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KillAuraI extends Check {

    public KillAuraI(final PlayerData data) {
        super(data, "KillAura", "I", "combat.killaura.i", "Checks for attack angle.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isUseEntity()) {
            final Player damager = data.getPlayer();
            final Entity victim = data.getCombatProcessor().getTarget();

            final double x1 = damager.getEyeLocation().getX();
            final double z1 = damager.getEyeLocation().getZ();
            final double vdX = damager.getEyeLocation().getDirection().getX();
            final double vdZ = damager.getEyeLocation().getDirection().getZ();
            final double x2 = victim.getLocation().getX();
            final double z2 = victim.getLocation().getZ();

            final double dotProduct = vdX * (x2 - x1) + vdZ * (z2 - z1);
            final double avMod = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
            final double vdMod = Math.sqrt(vdX * vdX + vdZ * vdZ);

            final double cosAngle = dotProduct / (avMod * vdMod);
            final int angle = (int) Math.toDegrees(Math.acos(cosAngle));

            final int totalTicks = Arctic.getInstance().getTicksManager().getTotalTicks();
            final int ticksMS = data.getConnectionProcessor().getKeepAlivePing() / 50;

            final Vector origin = data.getMovementProcessor().getLocation().toBukkitVec();

            //credits to medusa xD
            final double distance = data.getTargetLocations().stream()
                    .filter(pair -> Math.abs(totalTicks - pair.getSecond() - ticksMS) < 3)
                    .mapToDouble(pair -> {
                        final Vector victimVec = pair.getFirst().toVector().setY(0);
                        return victimVec.distance(origin.clone().setY(0)) - 0.3F;
                    }).min().orElse(0);


            debug("dist=" + distance + " angle=" + angle);

            if (distance > 1.25 && angle > 35) {
                buffer += angle;
                if (buffer >= 450)
                    fail("angle=" + angle);

            } else if (buffer >= 10) buffer -= 15;
        }

    }
}
