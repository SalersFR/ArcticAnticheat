package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class KillAuraG extends Check {

    public KillAuraG(PlayerData data) {
        super(data, "KillAura", "G", "combat.killaura.g", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final Entity target = event.getTarget();
                final Player player = data.getPlayer();

                //Angle check (TheHunter365's math btw)

                final double x1 = player.getEyeLocation().getX();
                final double z1 = player.getEyeLocation().getZ();

                final double vdX = player.getEyeLocation().getDirection().getX();
                final double vdZ = player.getEyeLocation().getDirection().getZ();

                final double x2 = target.getLocation().getX();
                final double z2 = target.getLocation().getZ();

                final double dotProduct = vdX * (x2 - x1) + vdZ * (z2 - z1);
                final double avMod = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));

                final double vdMod = Math.sqrt(vdX * vdX + vdZ * vdZ);
                final double cosAngle = dotProduct / (avMod * vdMod);

                final int angle = (int) Math.toDegrees(Math.acos(cosAngle));

                debug("angle=" + angle + " buffer=" + buffer);

                if (player.getLocation().toVector().clone().setY(0).distance(target.getLocation().toVector().clone().
                        setY(0)) > 0.5 && angle > 55) {
                    if (++buffer > 5) {
                        fail("angle=" + angle);
                    }

                } else if (buffer > 0) buffer -= 0.25D;
            }
        }
    }

}

