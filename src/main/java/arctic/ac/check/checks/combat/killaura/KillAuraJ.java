package arctic.ac.check.checks.combat.killaura;


import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Player;


public class KillAuraJ extends Check {


    public KillAuraJ(PlayerData data) {
        super(data, "KillAura", "J", "combat.killaura.j", "Checks for hitboxes glitch.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getWrapper().getTarget().isPresent()) {
                final Vector3d vec3D = event.getWrapper().getTarget().get();

                debug("x=" + Math.abs(vec3D.getX()) + " y=" + Math.abs(vec3D.getY()) + " z=" + Math.abs(vec3D.getZ()));

                if (Math.abs(vec3D.getX()) > 0.4f || Math.abs(vec3D.getY()) > 1.902 || Math.abs(vec3D.getZ()) > 0.4f
                        && event.getTarget() instanceof Player) {
                    if (++buffer > 0)
                        fail();

                } else if (buffer > 0) buffer -= 0.025D;


            }


        }
    }


}
