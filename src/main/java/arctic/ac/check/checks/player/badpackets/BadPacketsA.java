package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.EntityActionEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class BadPacketsA extends Check {

    private boolean sentSneak, sentSprint;

    public BadPacketsA(PlayerData data) {
        super(data, "BadPackets", "A", "player.badpackets.a", false);

    }

    @Override
    public void handle(Event e) {
        if (e instanceof EntityActionEvent) {

            final EntityActionEvent event = (EntityActionEvent) e;

            if (event.getAction().toString().toLowerCase().contains("sprint")) {
                sentSprint = true;
            }

            if (event.getAction().toString().toLowerCase().contains("sneak")) {
                sentSneak = true;
            }


        } else if (e instanceof FlyingEvent) {
            sentSneak = sentSprint = false;
        } else if (e instanceof UseEntityEvent) {


            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
                if ((sentSprint || sentSneak)) {
                    fail("sent sprint & sneak packet in the same tick");
                }
            }
        }

    }
}

