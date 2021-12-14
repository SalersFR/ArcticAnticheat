package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AimK extends Check {

    public AimK(PlayerData data) {
        super(data, "Aim", "K", "combat.aim.k", "Checks for lock-view modules.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if(packetType == PacketType.IN_LOOK ||packetType == PacketType.IN_POSITION_LOOK) {

            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            if(data.getCombatProcessor().getTarget() == null || data.getCombatProcessor().getHitTicks() >= 4) return;

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

            final double distance = ray().stream().mapToDouble(vec -> vec.clone().setY(0).
                    distance(damager.getEyeLocation().toVector().clone().setY(0)) - 0.5).min().orElse(0);

            debug("dist=" + distance + " angle=" + angle + " buffer=" + buffer);

            if(distance > 0.99D && angle <= 2 && rotationProcessor.getDeltaYaw() > 5.5F && data.getMovementProcessor().getDeltaXZ() >= 0.16D) {
                buffer += (5 - angle);
                if(buffer > 25) {
                    fail("angle=" + angle);
                }
            } else if(buffer > 0) buffer -= 0.5;



        }
    }

    private List<Vector> ray() {

        final List<Vector> toReturn = new ArrayList<>();

        try {
            for (int i = 0; i < 2; i++) {
                toReturn.add(data.getPastEntityLocations().get(i).toVector());
            }
        } catch (IndexOutOfBoundsException e) {

        }

        return toReturn;
    }

}
