package polar.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.EntityType;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.event.client.UseEntityEvent;

public class KillAuraB extends Check {

    private int ticksSinceLastAttack;
    private double lastDeltaXZ;


    public KillAuraB(PlayerData data) {
        super(data, "KillAura", "B", "combat.killaura.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            // Fixes a null error that fires at line 41.
            if (data.getInteractData().getTarget() == null) return;

            final MoveEvent moveEvent = (MoveEvent) e;

            final double deltaXZ = moveEvent.getDeltaXZ();

            final double xzAcceleration = Math.abs(deltaXZ - lastDeltaXZ);

            debug("ticksSinceLastAttack=" + this.ticksSinceLastAttack + "xzAccel=" + xzAcceleration +
                    " deltaXZ=" + deltaXZ + " lastDeltaXZ=" + lastDeltaXZ + " sprint=" + data.getInteractData().isSprinting());

            if (ticksSinceLastAttack <= 2
                    && xzAcceleration < 0.001D
                    && deltaXZ > 0.17
                    && data.getInteractData().isSprinting()
                    && data.getInteractData().getTarget().getType() == EntityType.PLAYER) {
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
