package honeybadger.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.ArmAnimationEvent;
import honeybadger.ac.event.client.UseEntityEvent;

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
