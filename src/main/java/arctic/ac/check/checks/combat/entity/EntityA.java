package arctic.ac.check.checks.combat.entity;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.WorldUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.Gravity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityA extends Check {

    /*
    An NPC check (KillAura).
     */

    /*
     *******
     * TODO
     *   - Make NPC teleportation faster
     *******
     */

    public EntityA(PlayerData data) {
        super(data, "Entity", "A", "combat.entity.a", true);
    }

    @Override
    public void handle(Event e) {
        if (data.getInteractData().getTicksSinceJoin() < 75) return;
        if (e instanceof UseEntityEvent) {
            UseEntityEvent event = (UseEntityEvent) e;


            if (event.getTarget() != null) {
                debug("name=" + event.getTarget().getName() + " buffer=" + buffer + " is=" + Arctic.INSTANCE.isCitizensPresent());
            }
            if (Arctic.INSTANCE.isCitizensPresent()) {

                if (event.getTarget() != null || event.getTarget().getName() != null) {
                    if (event.getTarget().getName().equalsIgnoreCase("BOT_Arctic")) { // Player attacked an NPC from Citizens
                        if (buffer++ >= 15) {
                            fail("buffer(hits)=" + buffer);
                        }
                    } else if (buffer > 0) {
                        buffer -= 0.25;
                    }
                }
            }
        } else if (e instanceof FlyingEvent) {
            if (Arctic.INSTANCE.isCitizensPresent()) {
                if (data.getInteractionData().getEntityANPC() == null
                        || !data.getInteractionData().getEntityANPC().isSpawned()) {
                    NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BOT_Arctic");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            npc.spawn(new WorldUtils()
                                    .getBehindPlayer(data.getBukkitPlayerFromUUID()).add(0, 3, 0));

                            npc.getTrait(Gravity.class).gravitate(true);
                        }
                    }.runTask(Arctic.INSTANCE);
                    data.getInteractionData().setEntityANPC(npc);
                } else {
                    data.getInteractionData().getEntityANPC()
                            .teleport(new WorldUtils()
                                            .getBehindPlayer(data.getBukkitPlayerFromUUID()).add(0, 3, 0),
                                    PlayerTeleportEvent.TeleportCause.PLUGIN);
                    data.getInteractData().getEntityANPC().getTrait(Gravity.class).gravitate(true);
                    data.getInteractionData().getEntityANPC().getTrait(Equipment.class).set(1, new ItemStack(Material.DIAMOND_HELMET));
                }
            }
        }
    }
}
