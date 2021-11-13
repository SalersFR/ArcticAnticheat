package arctic.ac.check.checks.combat.reach;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.tracker.EntityTracker;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.AEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Reach extends Check {


    public Reach(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", "Checking reach using interpolation and relmoves.",true);
    }

    public Location getEyeLocation() {

        Location eye = data.getPlayer().getLocation();

        eye.setY(eye.getY() + data.getPlayer().getEyeHeight());

        return eye;
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            if (data.getInteractData().getTarget() == null) return;

            final UseEntityEvent event = (UseEntityEvent) e;

            final EntityTracker entityTracker = data.getEntityTracker();

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                for (AEntity entities : entityTracker.getTrackedEntities()) {
                    if (entities.getId() == event.getTarget().getEntityId()) {
                        Bukkit.broadcastMessage("x=" + entities.getX() + " y=" + entities.getY() + " z=" + entities.getZ());
                    }
                }
            }
        }


    }
}
