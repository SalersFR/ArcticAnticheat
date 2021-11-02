package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;


public class MotionF extends Check {

    private double lastDeltaY;
    private int ticksSinceUpper;

    public MotionF(PlayerData data) {
        super(data, "Motion", "F", "movement.motion.f", "Checks for brutal y movement while being near ground.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final boolean nearGround = new WorldUtils().isOnGround(data.getPlayer().getLocation(), -0.3);
            final boolean exempt = data.getPlayer().getFallDistance() > 5F || data.getInteractData().getTicksAlive() < 35 || ticksSinceUpper < 10 || deltaY == 0;

            final double accel = Math.abs(deltaY - lastDeltaY);

            if (nearGround && !exempt && accel >= 0.699D) {
                if (++buffer > 1)
                    fail("accel=" + accel);

            } else if(buffer > 0) buffer -= 0.025D;

            if(new WorldUtils().isOnACertainBlock(data.getPlayer(), "slabs") || new WorldUtils().isOnACertainBlock(data.getPlayer(), "stairs"))
                this.ticksSinceUpper = 0;

            this.ticksSinceUpper++;

            debug("deltaY=" + deltaY +" lastDeltaY=" + lastDeltaY + " nearGround=" + nearGround + "\naccel=" + accel + " exempt=" + exempt);
        }

    }
}
