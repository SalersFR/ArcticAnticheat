package arctic.ac.check.checks.player.scaffold;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;

public class ScaffoldA extends Check {
    public ScaffoldA(PlayerData data) {
        super(data, "Scaffold", "A", "player.scaffold.a", true);
    }

    @Override
    public void handle(Event e) {
    }
}
