package arctic.ac.check.checks.movement.step;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;

public class StepB extends Check {

    public StepB(PlayerData data) {
        super(data, "Step", "B", "movement.step.b", "Checks for invalid step height (x2).", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            if (event.isGround() && event.getDeltaXZ() > 0.003 && event.getDeltaY() >= 1.2125D && data.getPlayer().getVehicle() == null
                    && data.getInteractData().getTicksSinceJoin() > 10) {
                fail("dY=" + event.getDeltaY());
            }
        }
    }
}
