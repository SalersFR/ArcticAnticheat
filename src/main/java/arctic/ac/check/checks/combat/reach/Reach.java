package arctic.ac.check.checks.combat.reach;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.tracker.ReachEntity;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.MathHelper;
import arctic.ac.utils.mc.AxisAlignedBB;
import arctic.ac.utils.mc.MovingObjectPosition;
import arctic.ac.utils.mc.Vec3;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Reach extends Check {


    public Reach(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", "Checking reach using interpolation and relmoves.", true);
    }

    public Location getEyeLocation() {

        Location eye = data.getPlayer().getLocation();

        eye.setY(eye.getY() + data.getPlayer().getEyeHeight());

        return eye;
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            if (data.getInteractData().getTarget() == null) return;

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final ReachEntity reachEntity = data.getEntityTracker().getEntityFromId.get(event.getTarget().getEntityId());
                final ALocation aLocation = data.getLocation();

                final AxisAlignedBB targetBox = new AxisAlignedBB(new Vector(reachEntity.getX(), reachEntity.getY(), reachEntity.getZ()));
                final Vec3 origin = getPositionEyes(aLocation.getX(), aLocation.getY(), aLocation.getZ(), data.getPlayer().getEyeHeight());
                Vec3 look = getVectorForRotation((float) data.getPlayer().getLocation().getPitch(), (float) data.getPlayer().getLocation().getYaw());
                look = origin.addVector(look.xCoord * 6,
                        look.yCoord * 6,
                        look.zCoord * 6);

                final MovingObjectPosition collision = targetBox.calculateIntercept(origin, look);

                final double reach = collision.hitVec.distanceTo(origin) - 0.11f;

                if(reach >= 3.05f) {
                    if(++buffer > (data.getNetworkProcessor().getKeepAlivePing() / 35)) {
                        fail("reach=" + reach);
                    }
                } else if(buffer > 0) buffer -= 0.025D;


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
