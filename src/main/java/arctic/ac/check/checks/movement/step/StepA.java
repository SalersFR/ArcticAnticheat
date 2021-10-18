package arctic.ac.check.checks.movement.step;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;

public class StepA extends Check {

    private int groundTicks;

    public StepA(PlayerData data) {
        super(data, "Step", "A", "movement.step.a", "Checks for invalid step height.", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();

            if (event.isGround()) {
                this.groundTicks++;
            } else this.groundTicks = 0;

            debug("groundTicks=" + groundTicks + " deltaY=" + deltaY);

            if (groundTicks > 2 && deltaY > 0.6D && data.getPlayer().getVehicle() == null
                    && data.getInteractData().getTicksSinceJoin() > 10) {
                fail("deltaY=" + deltaY);
            }
        }
    }
}
