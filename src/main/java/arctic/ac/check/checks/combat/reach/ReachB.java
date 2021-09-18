package arctic.ac.check.checks.combat.reach;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ReachB extends Check {

    private List<Vector> ray(final int ping) {

        final List<Vector> toReturn = new ArrayList<>();

        try {
            for (int i = 0; i < 2; i++) {
                toReturn.add(data.getPastEntityLocations().get(i + (ping / 50)).toVector());
            }
        }catch (IndexOutOfBoundsException e) {

        }

        return toReturn;
    }

    public ReachB(final PlayerData data) {
        super(data,"Reach","B","combat.reach.b",true);
    }
    @Override
    public void handle(Event e) {
        if(e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;
            if(event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                data.setTarget((LivingEntity) event.getTarget());

                final EntityPlayer nms = ((CraftPlayer)data.getPlayer()).getHandle();

                final List<Vector> pastVectors = ray(nms.ping);

                final Vector attacker = data.getBukkitPlayerFromUUID().getEyeLocation().toVector();

                final double distance = pastVectors.stream().mapToDouble(vec -> vec.clone().setY(0).distance(attacker.clone().setY(0)) - 0.56969D).min().orElse(0);

                debug(distance + "");

                if(distance > 3.1D) {
                    if(++buffer > 5) {
                        fail("distance=" + distance);
                    }
                } else if(buffer > 0) buffer -= 0.75D;



            }
        }
    }
}
