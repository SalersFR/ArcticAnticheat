package arctic.ac.check.checks.combat.reach;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ABox;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.ClientEntityLocations;
import arctic.ac.utils.EntityId;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;

public class Reach extends Check {


    public Reach(PlayerData data) {
        super(data, "Reach", "A", "combat.reach.a", "Checking reach using interpolation and relmoves.", true);
    }


    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            if (data.getInteractData().getTarget() == null) return;

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final ClientEntityLocations clientEntityLocations = data.getClientEntityLocations();

                final EntityId entity = clientEntityLocations.getEntityFromId.get(event.getTarget().getEntityId());

                final ABox hitbox = new ABox(new ALocation(entity.getX(), entity.getY(), entity.getZ()));
                final ALocation aLocation = data.getPosData().getALocation();

                final double distance = hitbox.distanceXZ(aLocation);

                Bukkit.broadcastMessage("distance=" + distance);


            }
        }


    }
}
