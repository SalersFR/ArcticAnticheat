package arctic.ac.check.checks.movement.predictions;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.ALocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PredictionA extends Check {

    public PredictionA(PlayerData data) {
        super(data, "prediction", "X", "movement.speed.x", "Predicts your nex move like a cat", false);
    }
    Vector lastMotion = new Vector(0, 0, 0);
    boolean pLastGround;
    boolean pLastLastGround;

    @Override
    public void handle(Event event) {
        if (event instanceof MoveEvent) {
            MoveEvent e = (MoveEvent) event;

            /* groundSystem */
            boolean onGround = e.isGround();
            boolean lastGround = pLastGround;
            boolean lastLastGround = pLastLastGround;

            pLastGround = onGround;
            pLastLastGround = lastGround;

            /* Giving numbers by the client */
            double motionY = e.getTo().getY() - e.getFrom().getY();
            double motionX = e.getTo().getX() - e.getFrom().getX();
            double motionZ = e.getTo().getZ() - e.getFrom().getZ();
            double motionXZ = e.getTo().toVector().setY(0).distance(e.getFrom().toVector().setY(0));

            /* MotionY Type */
            boolean jumping = !onGround && lastGround && motionY >= 0;
            boolean falling = !onGround && lastGround && motionY < 0;
            boolean flying = !onGround && !lastGround && !lastLastGround;
            boolean walking = onGround && lastGround;

            /* groundSystem */
            if (walking) {
                Vector motion = new Vector(motionX, motionY, motionZ);
                Vector lastMotion = this.lastMotion;
                this.lastMotion = motion;
                lastMotion.multiply(0.6);

                final float yaw = e.getTo().getYaw(); //TODO fix yaw
                final double yawRadians = Math.toRadians(yaw);

                final Vector look = new Vector(-Math.sin(yawRadians), 0.0, Math.cos(yawRadians));
                Vector move = motion.subtract(lastMotion);

                double angle = angle(look, move);
                float angleTo = (float) Math.toDegrees(angle);

                Vector predictedMotion = getPredMotion(e.getTo(),angleTo,e.getTo().getYaw(),move.distance(new Vector(0,0,0)),data.getPlayer().getWorld());
                //Bukkit.broadcastMessage("offSet " + predictedMotion.distance(motion));
            }
        }
    }

    public static double angle(Vector a, Vector b) {
        double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
    }

    private Vector getPredMotion(ALocation loc, float wasdKey, float yaw, double speed, World world) {
        Vector motion = null;

        motion = new Location(world,loc.getX() ,loc.getY() ,loc.getZ(),yaw + wasdKey,0).getDirection().normalize().multiply(speed);

        return motion;
    }
}
