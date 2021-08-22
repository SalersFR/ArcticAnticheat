package polar.ac.check.checks.combat.reach;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.data.impl.TargetTracker;
import polar.ac.event.Event;
import polar.ac.event.client.UseEntityEvent;
import polar.ac.utils.PLocation;

public class Reach extends Check {


    public Reach(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", true);
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

            final TargetTracker tracker = data.getTargetTracker();

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final PLocation targetLoc = new PLocation(tracker.getX(), tracker.getY(), tracker.getZ());

                final Location eyePos = data.getPlayer().getEyeLocation();

                final double distance = eyePos.toVector().clone().setY(0).distance(new Vector(tracker.getX(), 0, tracker.getZ()));

                debug("distance=" + distance + " targetPos=" + targetLoc.getX() + "," + targetLoc.getZ());


            }
        }


    }
}
