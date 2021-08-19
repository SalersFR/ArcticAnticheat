package honeybadger.ac.check.checks.combat.autoclicker;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.ArmAnimationEvent;

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
