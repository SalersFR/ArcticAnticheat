package honeybadger.ac.check.checks.combat.entity;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;

public class EntityA extends Check {

    /*
    An NPC check (KillAura).
     */

    public EntityA(PlayerData data) {
        super(data, "Entity", "A", "combat.entity.a", true);
    }

    @Override
    public void handle(Event e) {

    }
}
