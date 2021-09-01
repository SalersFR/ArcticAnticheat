package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import org.bukkit.Bukkit;

public class AimM extends Check {

    private double lastDeltaYaw;

    public AimM(PlayerData data) {
        super(data, "Aim","M","combat.aim.m",true);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double deltaYaw = event.getDeltaYaw();
            final double lastDeltaYaw = this.lastDeltaYaw;

            this.lastDeltaYaw = deltaYaw;

            final boolean exempt = deltaYaw == lastDeltaYaw;

            final double accel = Math.abs(deltaYaw - lastDeltaYaw);

            if(!exempt) {
                debug("accel=" + accel);
                if(accel < 0.004 && deltaYaw > 10) {
                    if(++buffer > 6) {
                        fail("accel=" + accel + " dy=" + deltaYaw);
                    }
                } else if(buffer > 0) buffer -= 0.75D;
            }
        }

    }
}
