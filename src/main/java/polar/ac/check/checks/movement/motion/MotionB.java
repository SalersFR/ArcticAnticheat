package polar.ac.check.checks.movement.motion;

import org.bukkit.Bukkit;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.MathUtils;
import polar.ac.utils.WorldUtils;

public class MotionB extends Check {
    public MotionB(PlayerData data) {
        super(data, "Motion", "B", "movement.motion.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            MoveEvent event = (MoveEvent) e;

            String dir = new WorldUtils().getCardinalDirection(data.getBukkitPlayerFromUUID());

            if (data.getInteractionData().isSprinting()) {
                switch (dir) {
                    case "S":
                        if (event.getDeltaZ() < 0 && !(Math.abs(event.getDeltaZ()) <= 0.04)) {
                            if (++buffer > 4) {
                                fail("deltaZ=" + event.getDeltaZ());
                            }
                        } else if (buffer > 0) buffer -= 0.25;
                        break;
                    case "N":
                        if (event.getDeltaZ() > 0 && !(Math.abs(event.getDeltaZ()) <= 0.04)) {
                            if (++buffer > 4) {
                                fail("deltaZ=" + event.getDeltaZ());
                            }
                        } else if (buffer > 0) buffer -= 0.25;
                        break;
                    case "E":
                        if (event.getDeltaX() < 0 && !(Math.abs(event.getDeltaX()) <= 0.04)) {
                            if (++buffer > 4) {
                                fail("deltaX=" + event.getDeltaX());
                            }
                        } else if (buffer > 0) buffer -= 0.25;
                        break;
                    case "W":
                        if (event.getDeltaX() > 0 && !(Math.abs(event.getDeltaX()) <= 0.04)) {
                            if (++buffer > 4) {
                                fail("deltaX=" + event.getDeltaX());
                            }
                        } else if (buffer > 0) buffer -= 0.25;
                        break;
                }
            }
        }
    }
}
