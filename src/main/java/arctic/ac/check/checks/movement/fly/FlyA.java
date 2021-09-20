package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.event.client.PacketEvent;
import arctic.ac.utils.WorldUtils;
import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FlyA extends Check {

    private double lastDeltaY, airTicks,ticksEdge,ticksPlace;

    public FlyA(PlayerData data) {
        super(data, "Fly", "A", "movement.fly.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final WorldUtils worldUtils = new WorldUtils();

            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            /*final*/
            double predictedDeltaY = (lastDeltaY - 0.08) * 0.98F;


            if (Math.abs(predictedDeltaY) < 0.005) {
                predictedDeltaY = 0;
            }

            final double result = Math.abs(deltaY - predictedDeltaY);


            final Player player = data.getBukkitPlayerFromUUID();

            if(worldUtils.isAtEdgeOfABlock(player))  {
                this.ticksEdge = 0;
            }else this.ticksEdge++;

            this.ticksPlace++;

            double threshold = 0.01;

            if(ticksPlace < 20 || player.hasPotionEffect(PotionEffectType.JUMP)) {
                threshold = 0.032;
            } else if(threshold == 0.032) {
                threshold = 0.01;
            }

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || airTicks < 12
                    || player.getFallDistance() > 50.0F
                    || event.isGround();

            debug("result=" + result + " exempt=" + exempt + " deltaY=" + deltaY + " lastDeltaY=" + lastDeltaY + " airTicks=" + airTicks);


            if (result > threshold && !exempt && !worldUtils.isCloseToGround(player.getLocation())) {
                if (++buffer > 3) {
                    fail("result=" + result);

                }

            } else if (buffer > 0) buffer -= 0.05D;


        } else if(e instanceof PacketEvent) {

            final PacketEvent event = (PacketEvent) e;

            if(event.getPacketType() == PacketType.Play.Client.BLOCK_PLACE) {
                this.ticksPlace = 0;
            }
        }
    }


}
