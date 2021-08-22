package polar.ac.check.checks.combat.autoclicker;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.ArmAnimationEvent;

public class AutoclickerA extends Check {
    public AutoclickerA(PlayerData data) {
        super(data, "Autoclicker", "A", "combat.autoclicker.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            debug("armsent=" + System.currentTimeMillis() + " " +
                    "digging=" + data.getInteractionData().isDigging());
        }
    }
}
