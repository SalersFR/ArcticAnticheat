package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class KillAuraD extends Check {

    private int attacksWithoutSwing;

    public KillAuraD(PlayerData data) {
        super(data, "KillAura", "D", "combat.killaura.d", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            this.attacksWithoutSwing = 0;
        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            debug("attacksWithoutSwing" + attacksWithoutSwing);

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
                if (++this.attacksWithoutSwing > 3) {
                    fail("attacks=" + attacksWithoutSwing);
                }
            }

        }
    }
}
