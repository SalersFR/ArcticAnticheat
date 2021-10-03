package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionB extends Check {

    private int ticksSinceIce,ticksSinceNearHead;

    public MotionB(PlayerData data) {
        super(data, "Motion", "B", "movement.motion.b", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final World world = data.getBukkitPlayerFromUUID().getWorld();

            final WorldUtils worldUtils = new WorldUtils();
            final double deltaY = event.getDeltaY();

            final Location bukkitTo = event.getTo().toVector().toLocation(world);
            final Location bukkitFrom = event.getFrom().toVector().toLocation(world);

            final Player player = data.getPlayer();

            final boolean jumped = worldUtils.isOnGround(bukkitFrom, -0.00001) && !worldUtils.isOnGround(bukkitTo, -0.00001) && deltaY > 0;

            if (worldUtils.isOnACertainBlock(player, "ice"))
                ticksSinceIce = 0;

            final float predicted = 0.42F;
            final float fixedPredicted = player.hasPotionEffect(PotionEffectType.JUMP) ? predicted
                    + ((PlayerUtils.getPotionLevel(player, PotionEffectType.JUMP)) * 0.1F) - 0.0001F : predicted;

            if(worldUtils.blockNearHead(bukkitTo)) {
                this.ticksSinceNearHead = 0;
            }

            final boolean exempt = worldUtils.blockNearHead(bukkitTo) || worldUtils.isCollidingWithClimbable(player)
                    || data.getInteractData().isTeleported() || data.getInteractionData().getTicksSinceHurt() < 40
                    || ticksSinceIce < 15 || ticksSinceNearHead < 15    || data.getInteractionData().getTicksSinceSlime() < 60;

            debug("jumped=" + jumped + " deltaY=" + deltaY + " predicted=" + predicted);

            if (jumped && deltaY < fixedPredicted && bukkitFrom.getY() % 1 == 0 && !exempt) {
                fail("d=" + deltaY + " p=" + fixedPredicted);
            }


            this.ticksSinceIce++;
            this.ticksSinceNearHead++;


        }
    }
}
