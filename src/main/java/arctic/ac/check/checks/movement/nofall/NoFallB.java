package arctic.ac.check.checks.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NoFallB extends Check {


    public NoFallB(PlayerData data) {
        super(data, "NoFall", "B", "movement.nofall.b", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final World world = data.getPlayer().getWorld();
            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();

            final ALocation to = event.getTo();
            final ALocation from = event.getFrom();

            final boolean client = event.isGround();
            final boolean math = to.isMathOnGround() && from.isMathOnGround();
            final boolean coll = to.isCollOnGround(world) && from.isCollOnGround(world);

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "fence")
                    || data.getInteractData().getTicksSinceHurt() < 30
                    || player.getVehicle() != null;


            if (client && (!math || !coll) && !exempt) {
                if (++buffer > 4)
                    fail("spoofing flying ground\ncoll=" + coll + "\nmath=" + math);
            } else if (buffer > 0) buffer -= 0.25D;

            if (math && !coll && !exempt) {
                if (++buffer > 4)
                    fail("spoofing math ground\nflying=" + client);
            } else if (buffer > 0) buffer -= 0.5D;


        }
    }
}
