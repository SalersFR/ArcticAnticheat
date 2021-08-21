package honeybadger.ac.check.checks.combat.reach;

import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.data.impl.TargetTracker;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.UseEntityEvent;
import honeybadger.ac.utils.HBox;
import honeybadger.ac.utils.HLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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

                final HBox targetBox = new HBox(tracker.getX(), tracker.getY(), tracker.getZ());

                final Location eyePos = getEyeLocation();



                final double distance = eyePos.toVector().clone().setY(0).distance(new Vector(tracker.getX(),0,tracker.getZ()));

                debug("distance=" + distance);




            }
        }


    }
}
