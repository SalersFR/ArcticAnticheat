package polar.ac.check.checks.combat.entity;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
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
                    buffer = 0;
                } else {
                    if (buffer++ >= 5) {
                        fail("npcNotHit=" + buffer);
                    }
                }
            }
        } else if (e instanceof MoveEvent) {
            MoveEvent event = (MoveEvent) e;

            if (data.getInteractionData().getEntityANPC() == null
                    || !data.getInteractionData().getEntityANPC().isSpawned()) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BOT");
                npc.teleport(data.getBukkitPlayerFromUUID().getLocation().subtract(0, -5, -3).clone(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } else {
                data.getInteractionData().getEntityANPC().teleport(new WorldUtils().getBehindPlayer(data.getBukkitPlayerFromUUID()
                ), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }
}
