package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KillAuraI1 extends Check {

    private int lastAngle;

    public KillAuraI1(final PlayerData data) {
        super(data, "KillAura", "I1", "combat.killaura.i1", "Checks for attack angle.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isUseEntity()) {

            //checks if he is attacking
            if(data.getCombatProcessor().getHitTicks() > 1) return;

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
                        final Vector victimVec = pair.getFirst().setY(0);
                        return victimVec.distance(origin.clone().setY(0)) - 0.3F;
                    }).min().orElse(0);


            debug("dist=" + distance + " angle=" + angle + " buffer=" + buffer);

            if (distance > 1.55 && angle <= 3 && Math.abs(angle - lastAngle) <= 1 && data.getRotationProcessor().
                    getDeltaYaw() > 5.f && data.getMovementProcessor().getDeltaXZ() > 0.1525
                    && data.getRotationProcessor().getDeltaPitch() >= 0.2f) {
                buffer += Math.abs(2 - angle);
                if(buffer > 20)
                    fail("angle=" + angle);
            } else if(buffer > 0) buffer -= 0.1D;

            this.lastAngle = angle;
        }

    }
}
