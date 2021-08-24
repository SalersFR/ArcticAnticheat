package polar.ac.check.checks.player.badpackets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.EntityActionEvent;
import polar.ac.event.client.FlyingEvent;

public class BadPacketsA extends Check {

    private boolean sentSneak,sentSprint;

    public BadPacketsA(PlayerData data) {
        super(data,"BadPackets","A","player.badpackets.a",false);

    }
    @Override
    public void handle(Event e) {
        if(e instanceof EntityActionEvent) {

            final EntityActionEvent event = (EntityActionEvent) e;

            if(event.getAction().toString().toLowerCase().contains("sprint")) {
                sentSprint = true;
            }

            if(event.getAction().toString().toLowerCase().contains("sneak")) {
                sentSneak = true;
            }

            if(sentSneak && sentSprint) {
                fail("sent sprint & sneak packet in the same tick");
            }




        }else if(e instanceof FlyingEvent) {
            sentSneak = sentSprint = false;
        }

    }
}
