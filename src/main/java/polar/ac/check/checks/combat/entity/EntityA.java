package polar.ac.check.checks.combat.entity;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import polar.ac.Polar;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
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
            if (Polar.INSTANCE.isCitizensPresent()) {
                UseEntityEvent event = (UseEntityEvent) e;
                if (event.getTarget().hasMetadata("NPC")) { // Player attacked an NPC from Citizens
                    if (buffer++ >= 15) {
                        fail("buffer(hits)=" + buffer);
                    }
                } else if (buffer > 0) {
                    buffer -= 0.25;
                }
            }
        } else if (e instanceof MoveEvent) {
            MoveEvent event = (MoveEvent) e;

            if (data.getInteractionData().getEntityANPC() == null
                    || !data.getInteractionData().getEntityANPC().isSpawned()) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BOT");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        npc.spawn(new WorldUtils().getBehindPlayer(data.getBukkitPlayerFromUUID()));
                    }
                }.runTask(Polar.INSTANCE);
                data.getInteractionData().setEntityANPC(npc);
            } else {
                data.getInteractionData().getEntityANPC()
                        .teleport(new WorldUtils().getBehindPlayer(data.getBukkitPlayerFromUUID()).clone().add(0, 3, 0),
                                PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }
}
