package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;

public class FlyD extends Check {

    private double lastDeltaY, airTicks;
    private long lastTeleported;
    private double buffer;

    public FlyD(PlayerData data) {
        super(data, "Fly", "D", "movement.fly.d", "Checks for jumping while being in the air.", true);
    }

    public static boolean check(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.001;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MoveEvent) {
            MoveEvent e = (MoveEvent) event;
            double motionY = e.getDeltaY();
            double lastMotionY = lastDeltaY;
            this.lastDeltaY = motionY;

            double predictedMotionY = (lastMotionY - 0.08D) * 0.9800000190734863D;
            if (!e.isGround()) {
                airTicks++;
            } else airTicks = 0;
            final WorldUtils worldUtils = new WorldUtils();
            boolean onGroundV2 = worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation());

            if (data.getPlayer().getVelocity().getY() > -0.7) return;
            if (data.getPlayer().getAllowFlight()) return;
            if (data.getPosData().isTeleporting()) return;

            if (airTicks >= 3 && !onGroundV2 && Math.abs(predictedMotionY) >= 0.005D) {
                if (!check(motionY, predictedMotionY)) {
                    buffer += 10;
                    if (buffer > 30) {
                        fail("motionY " + motionY + " predicted " + predictedMotionY);
                    }
                } else {
                    if (buffer > 0) buffer--;
                }
            }
        }
    }
}
