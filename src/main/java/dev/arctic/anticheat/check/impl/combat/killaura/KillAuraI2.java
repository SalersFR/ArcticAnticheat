package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class KillAuraI2 extends Check {

    private double motionXZ;
    private double deltaYaw;

    public KillAuraI2(final PlayerData data) {
        super(data, "KillAura", "I2", "combat.killaura.i2", "Checks for consistent attack angle.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isUseEntity()) {

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

            final double distance = ray(data.getConnectionProcessor().getKeepAlivePing()).stream().mapToDouble(
                    vec -> vec.clone().setY(0).distance(damager.getEyeLocation().toVector().clone().setY(0)) - 0.45D).min().orElse(0);

            debug("dist=" + distance + " angle=" + angle);

            if(motionXZ > 0.16 && distance > 1.05D && angle <= 1 && deltaYaw > 6.f) {
                buffer += Math.abs(2 - angle);
                if(buffer > 12.25)
                    fail("angle");
            } else if(buffer > 0) buffer -= (10 / 3D);




        } else if(packet.isFlying()) {
            motionXZ = data.getMovementProcessor().getDeltaXZ();
        } else if(packet.isRotation()) {
            deltaYaw = data.getRotationProcessor().getDeltaYaw();
        }
    }

    private List<Vector> ray(final int ping) {

        final List<Vector> toReturn = new ArrayList<>();

        try {
            for (int i = 0; i < 2; i++) {
                toReturn.add(data.getTargetLocations().get(i + (ping / 50)).getFirst());
            }
        } catch (IndexOutOfBoundsException e) {

        }

        return toReturn;
    }
}
