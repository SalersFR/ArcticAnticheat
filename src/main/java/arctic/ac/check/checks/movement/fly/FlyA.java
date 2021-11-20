package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.event.client.PacketEvent;
import arctic.ac.utils.WorldUtils;
import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;

public class FlyA extends Check {

    private double lastDeltaY, airTicks, ticksEdge, ticksPlace;

    public FlyA(PlayerData data) {
        super(data, "Fly", "A", "movement.fly.a", "Checks if player is not taking care about gravity.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            final double deltaX = event.getDeltaX();
            final double deltaZ = event.getDeltaZ();

            this.lastDeltaY = deltaY;

            final double prediction = (lastDeltaY - 0.08F) * 0.98F;
            final double fivePrediction = Math.abs(prediction) < 0.005 ? 0 : prediction;

            final boolean zeroZeroThree = (Math.abs(deltaX) <= 0.03 || Math.abs(deltaZ) <= 0.03) && Math.abs(fivePrediction) <= 0.03;

            double threePrediction = prediction;
            double fixedThreePrediction = fivePrediction;

            double fixedPrediction = fivePrediction;

            if (zeroZeroThree) {
                threePrediction = (fivePrediction - 0.08F) * 0.98F;
                fixedThreePrediction = Math.abs(threePrediction) < 0.005 ? 0 : threePrediction;

                if (Math.abs(fixedThreePrediction - deltaY) <= 0.00001D)
                    fixedPrediction = fivePrediction;

            }

            if (event.isGround())
                this.airTicks = 0;
            else this.airTicks++;

            final Player player = data.getPlayer();
            final WorldUtils worldUtils = new WorldUtils();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "stairs")
                    || worldUtils.isOnACertainBlock(player, "ice")
                    || data.getInteractData().isHurt()
                    || worldUtils.haveABlockNearHead(player)
                    || data.getVelocityData().getVelocityTicks() <= 2;

            final double threshold = airTicks > 7 ? 0.001 : 0.01;

            if (airTicks > 1 && Math.abs(fixedPrediction - deltaY) > threshold && event.getDeltaXZ() > 0.001D && !exempt && ticksPlace > 7) {
                if (++buffer > 1.4D)
                    fail("diff=" + Math.abs(fixedPrediction - deltaY));
            } else if (buffer > 0) buffer -= 0.2D;

            debug("diff=" + Math.abs(fixedPrediction - deltaY));

            this.ticksPlace++;

        } else if (e instanceof PacketEvent) {

            final PacketEvent event = (PacketEvent) e;

            if (event.getPacketType() == PacketType.Play.Client.BLOCK_PLACE) {
                this.ticksPlace = 0;
            }
        }
    }


}
