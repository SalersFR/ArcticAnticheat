package polar.ac.check.checks.player.scaffold;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;

public class ScaffoldA extends Check {
    @Override
    public void handle(Event e) {

    }

    public ScaffoldA(PlayerData data) {
        super(data, "Scaffold", "A", "player.scaffold.a", true);
    }
}
