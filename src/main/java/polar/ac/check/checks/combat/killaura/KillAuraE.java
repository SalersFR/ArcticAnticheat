package polar.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.ArmAnimationEvent;
import polar.ac.event.client.UseEntityEvent;

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
                    if (++buffer > 3) {
                        fail("attacks=" + attacks);
                    } else if (buffer > 0) buffer -= 0.5D;
                }

                this.swings = attacks = 0;
            }


        }
    }
}
