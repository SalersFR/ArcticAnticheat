package polar.ac.check.checks.combat.entity;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Gravity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import polar.ac.Polar;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.FlyingEvent;
import polar.ac.event.client.MoveEvent;
import polar.ac.event.client.UseEntityEvent;
import polar.ac.utils.WorldUtils;

public class EntityA extends Check {

    /*
    An NPC check (KillAura).
     */

    public EntityA(PlayerData data) {
        super(data, "Entity", "A", "combat.entity.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            UseEntityEvent event = (UseEntityEvent) e;
            debug("name=" + event.getTarget().getName() + " buffer=" +buffer + " is=" + Polar.INSTANCE.isCitizensPresent());
            if (Polar.INSTANCE.isCitizensPresent()) {

                if ( event.getTarget().getName().equalsIgnoreCase("BOT_Polar")) { // Player attacked an NPC from Citizens
                    if (buffer++ >= 15) {
                        fail("buffer(hits)=" + buffer);
                    }
                } else if (buffer > 0) {
                    buffer -= 0.25;
                }
            }
        } else if (e instanceof FlyingEvent) {


            if (data.getInteractionData().getEntityANPC() == null
                    || !data.getInteractionData().getEntityANPC().isSpawned()) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BOT_Polar");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        npc.spawn(new WorldUtils().getBehindPlayer(data.getBukkitPlayerFromUUID()));
                        data.getInteractData().getEntityANPC().getTrait(Gravity.class).gravitate(true);

                    }
                }.runTask(Polar.INSTANCE);
                data.getInteractionData().setEntityANPC(npc);
            } else {
                data.getInteractionData().getEntityANPC()
                        .teleport(new WorldUtils().getBehindPlayer(data.getBukkitPlayerFromUUID()),
                                PlayerTeleportEvent.TeleportCause.PLUGIN);
                data.getInteractData().getEntityANPC().getTrait(Gravity.class).gravitate(true);
            }
        }
    }
}
