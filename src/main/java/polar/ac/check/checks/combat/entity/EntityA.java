package polar.ac.check.checks.combat.entity;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;

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
