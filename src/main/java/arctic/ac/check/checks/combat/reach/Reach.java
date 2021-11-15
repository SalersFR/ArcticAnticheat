package arctic.ac.check.checks.combat.reach;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.*;
import arctic.ac.utils.mc.AxisAlignedBB;
import arctic.ac.utils.mc.MovingObjectPosition;
import arctic.ac.utils.mc.Vec2f;
import arctic.ac.utils.mc.Vec3;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

public class Reach extends Check {


    public Reach(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", "Checking reach using interpolation and relmoves.", true);
    }


    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            if (data.getInteractData().getTarget() == null) return;

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final ClientEntityLocations clientEntityLocations = data.getClientEntityLocations();

                final EntityId current = clientEntityLocations.getEntityFromId.get(event.getTarget().getEntityId());
                final ALocation aLocation = data.getPosData().getALocation();


                AxisAlignedBB targetBox = new AxisAlignedBB(new Vector(current.getX(), current.getY(), current.getZ()));
                Vec3 origin = getPositionEyes(aLocation.getX(),
                        aLocation.getY(), aLocation.getZ(), data.getPlayer().getEyeHeight());
                Vec3 look = getVectorForRotation((float) data.getPlayer().getLocation().getPitch(), (float) data.getPlayer().getLocation().getYaw());
                look = origin.addVector(look.xCoord * 6,
                        look.yCoord * 6,
                        look.zCoord * 6);

                MovingObjectPosition collision = targetBox.calculateIntercept(origin, look);

                final double distance = collision.hitVec.distanceTo(origin) - 0.1f;

                Bukkit.broadcastMessage("dist=" + distance);


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
