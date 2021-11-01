package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;


public class MotionF extends Check {

    private double lastDeltaY;

    public MotionF(PlayerData data) {
        super(data, "Motion", "F", "movement.motion.f", "Checks for brutal y movement while being near ground..", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final boolean nearGround = new WorldUtils().isOnGround(data.getPlayer().getLocation(), -0.3);
            final boolean exempt = data.getPlayer().getFallDistance() > 5F;

            final double accel = Math.abs(deltaY - lastDeltaY);

            if (nearGround && !exempt && accel >= 0.53D) {
                if (++buffer > 1)
                    fail("accel=" + accel);

            } else if(buffer > 0) buffer -= 0.025D;

            debug("deltaY=" + deltaY +" lastDeltaY=" + lastDeltaY + " nearGround=" + nearGround + "\naccel=" + accel + " exempt=" + exempt);
        }

    }
}
