package honeybadger.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.MoveEvent;
import honeybadger.ac.event.client.UseEntityEvent;

public class KillAuraB extends Check {

    private int ticksSinceLastAttack;
    private double lastDeltaXZ;


    public KillAuraB(PlayerData data) {
        super(data, "KillAura", "B", "combat.killaura.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent moveEvent = (MoveEvent) e;

            final double deltaXZ = moveEvent.getDeltaXZ();

            final double xzAcceleration = Math.abs(deltaXZ - lastDeltaXZ);

            debug("ticksSinceLastAttack=" + this.ticksSinceLastAttack + "xzAccel=" + xzAcceleration +
                    " deltaXZ=" + deltaXZ + " lastDeltaXZ=" + lastDeltaXZ + " sprint=" + data.getInteractData().isSprinting());

            if (ticksSinceLastAttack <= 2
                    && xzAcceleration < 0.001D
                    && deltaXZ > 0.17
                    && data.getInteractData().isSprinting()) {
                if (++this.buffer > 4.5) {
                    fail("xzAccel=" + xzAcceleration);
                }

            } else this.buffer *= 0.8;


            this.ticksSinceLastAttack++;

            this.lastDeltaXZ = deltaXZ;


        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
                this.ticksSinceLastAttack = 0;


            }
        }
    }
}
