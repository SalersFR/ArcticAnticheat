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

public class MotionF extends Check {

    private double motionY;
    private boolean lastGround;
    private int airTicks;

    public MotionF(PlayerData data) {
        super(data, "Motion", "F", "movement.motion.f", "Checks if player is following some mc rules.", true);
    }


    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;
            final WorldUtils worldUtils = new WorldUtils();

            if (this.motionY < 0.005)
                motionY = 0.0D;

            final boolean ground = event.isGround();
            final boolean lastGround = this.lastGround;

            this.lastGround = ground;

            final Player player = data.getPlayer();
            final World world = player.getWorld();

            final Location bukkitTo = event.getTo().toVector().toLocation(world);
            final Location bukkitFrom = event.getFrom().toVector().toLocation(world);

            if(ground)
                airTicks = 0;
            else airTicks++;

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player,"stairs")
                    || worldUtils.isOnACertainBlock(player,"ice")
                    || data.getInteractData().isHurt()
                    || data.getInteractData().getTicksSinceHurt() < 20
                    || worldUtils.haveABlockNearHead(player)
                    || airTicks >= 8
                    || bukkitTo.add(0,-0.65D,0).getBlock().isEmpty();

            final double deltaY = event.getDeltaY();

            final boolean jumped = lastGround &&
                    !ground && deltaY > 0;

            if(jumped)
                jump();

            if(!lastGround && !ground) {
                motionY -= 0.08F;
                motionY *= 0.98F;
            }

            final float diff = (float) Math.abs(motionY - deltaY);

            if(diff > 0.001 && !exempt) {
                if(buffer < 3) buffer++;
                if(buffer > 1)
                    fail("diff=" + diff);
            } else if(buffer > 0) buffer -= 0.2D;

            debug("diff=" + diff + " mot=" + motionY + "delt=" + deltaY);






        }
    }
    protected void jump() {

        this.motionY = (double) 0.42F;

        if (data.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
            motionY += (PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) * 0.1F);
        }


    }
}
