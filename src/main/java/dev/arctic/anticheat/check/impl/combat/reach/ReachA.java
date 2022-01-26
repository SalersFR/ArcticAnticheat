package dev.arctic.anticheat.check.impl.combat.reach;

import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CombatProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.mc.AxisAlignedBB;
import dev.arctic.anticheat.utilities.mc.MathHelper;
import dev.arctic.anticheat.utilities.mc.MovingObjectPosition;
import dev.arctic.anticheat.utilities.mc.Vec3;
import org.bukkit.util.Vector;

public class ReachA extends Check {

    public ReachA(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", "Checks for an invalid attack reach.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CombatProcessor combatProcessor = data.getCombatProcessor();
            if(combatProcessor.getTarget() == null || combatProcessor.getLastTarget() == null) {
                return;
            }
            if (combatProcessor.getHitTicks() == 1 && combatProcessor.getTarget().getEntityId() == combatProcessor.getLastTarget().getEntityId()) {
                final int totalTicks = Arctic.getInstance().getTicksManager().getTotalTicks();
                final int ticksMS = data.getConnectionProcessor().getKeepAlivePing() / 50;

                final Vector originLoc = data.getMovementProcessor().getLocation().toBukkitVec();


                //credits to medusa xD
                double distance = data.getTargetLocations().stream()
                        .filter(pair -> Math.abs(totalTicks - pair.getSecond() - ticksMS) < 4)
                        .mapToDouble(pair -> {

                            final Vector victimVec = pair.getFirst();
                            final AxisAlignedBB targetBox = new AxisAlignedBB(victimVec);

                            Vec3 origin = getPositionEyes(originLoc.getX(),
                                    originLoc.getY(), originLoc.getZ(), data.getPlayer().getEyeHeight());
                            Vec3 look = getVectorForRotation((float) data.getRotationProcessor().getPitch(), (float) data.getRotationProcessor().getYaw());
                            look = origin.addVector(look.xCoord * 6,
                                    look.yCoord * 6,
                                    look.zCoord * 6);


                            MovingObjectPosition collision = targetBox.calculateIntercept(origin, look);

                            return (collision == null || collision.hitVec == null || look == null) ? victimVec.clone().setY(0).
                                    distance(originLoc.clone().setY(0)) - 0.5f : collision.hitVec.distanceTo(origin) - 0.225f;

                        }).min().orElse(0);

                debug("reach=" + (distance > 3.05 ? "&c" : "") + (float) distance + " buffer=" + buffer);

                //don't ask
                if(data.getMovementProcessor().getDeltaXZ() <= 0.0313f)
                    distance -= 0.03125f;


                if (distance > 3.025) {
                    if (++buffer > 3.5) {
                        fail("reach=" + (float) distance);
                    }
                } else if (buffer > 0 && distance >= 0.185) buffer -= (distance * 0.0094);

            }
        }

    }

    public final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public Vec3 getPositionEyes(final double x, double y, double z, double eyeHeight) {
        return new Vec3(x, y + eyeHeight, z);
    }
}
