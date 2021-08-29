package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class KillAuraE extends Check {

    //Got the idea from GladUrBad

    private int attacks, swings;

    public KillAuraE(PlayerData data) {
        super(data, "KillAura", "E", "combat.killaura.e", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
                this.attacks++;

            }
        } else if (e instanceof ArmAnimationEvent) {
            this.swings++;
            if (swings >= 100) {
                /**
                 * TODO CHECK IF TARGET IS AFK / NOT MOVING
                 */
                if (++attacks > 89) {
                    if (++buffer > 12) {
                        fail("attacks=" + attacks);
                    } else if (buffer > 0) buffer -= 0.5D;
                }

                this.swings = attacks = 0;
            }


        }
    }
}
