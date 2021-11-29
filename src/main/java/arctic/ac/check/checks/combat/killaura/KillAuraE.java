package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ArcticQueue;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.Location;

public class KillAuraE extends Check {

    //Got the idea from GladUrBad

    private int attacks, swings;
    private ArcticQueue<Location> pastHittedLocations = new ArcticQueue<Location>(11);

    public KillAuraE(PlayerData data) {
        super(data, "KillAura", "E", "combat.killaura.e", "Checks for too high accuracy.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                this.attacks++;
                this.pastHittedLocations.add(event.getTarget().getLocation());

            }
        } else if (e instanceof ArmAnimationEvent) {
            this.swings++;
            if (swings >= 100) {

                if (pastHittedLocations.size() < 3) return;

                final Location current = pastHittedLocations.get(0);
                final Location past = pastHittedLocations.get(3);

                final boolean moved = past.getYaw() != current.getYaw() && past.getPitch() != current.getPitch() && past.distance(current) > 0.01D;
                if (++attacks > 89 && moved) {
                    if (++buffer > 4) {
                        fail("attacks=" + attacks);
                    } else if (buffer > 0) buffer -= 0.5D;
                }

                this.swings = attacks = 0;
            }


        }
    }
}
