package polar.ac.check.checks.movement.step;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;

public class StepA extends Check {

    private int groundTicks;

    public StepA(PlayerData data) {
        super(data,"Step","A","movement.step.a",false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();

            if(event.isGround()) {
                this.groundTicks++;
            } else this.groundTicks = 0;

            debug("groundTicks=" + groundTicks + " deltaY=" + deltaY);

            if(groundTicks > 2 && deltaY > 0.6D) {
                fail("deltaY=" + deltaY);
            }
        }
    }
}
