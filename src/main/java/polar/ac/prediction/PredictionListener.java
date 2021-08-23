package polar.ac.prediction;

import org.bukkit.Location;
import polar.ac.data.impl.PredictionData;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.PLocation;

public class PredictionListener {

    public float getFriction(Location loc) {
        try {
            String block = loc.add( 0, -1, 0).getBlock().getType().name().toLowerCase();
            return 0.91f * (block.equals("blue_ice") ? 0.989f : block.contains("ice") ? 0.98f : block.contains("slime") ? 0.8f : 0.6f);
        } catch (Exception ignored) {
            return 0.91f * 0.6f;
        }
    }

    public void onMove(MoveEvent event, PredictionData data) {

        PLocation to = event.getTo();
        PLocation from = event.getFrom();
        final double moveX = to.getX() - from.getX() - data.dDeltaX;
        final double moveZ = to.getZ() - from.getZ() - data.dDeltaZ;
        data.dDeltaX = (to.getX() - from.getX()) * (inLava ? 0.5 : event.isGround() ? getFriction(from) : 0.91f);
        data.dDeltaZ = (to.getZ() - from.getZ()) * (inLava ? 0.5 :event.isGround() ? getFriction(from) : 0.91f);
        final double move = Math.hypot(moveX, moveZ);
        if (move > 0.01) {
            double d = Double.MAX_VALUE;
            int f = 0;
            for (int yaw = 0; yaw < 360; yaw += 45) {
                double x = Math.cos((to.getYaw() + yaw) * Math.PI / 180) * move;
                double z = Math.sin((to.getYaw() + yaw) * Math.PI / 180) * move;
                double diff = Math.sqrt((moveX - x) * (moveX - x) + (moveZ - z) * (moveZ - z));
                if (diff < d) {
                    d = diff;
                    f = yaw;
                }
            }
            switch (f) {
                case 0:
                    forward = 0f;
                    strafe = 1f;
                    break;
                case 45:
                    forward = strafe = 1f;
                    break;
                case 90:
                    forward = 1f;
                    strafe = 0f;
                    break;
                case 135:
                    forward = 1f;
                    strafe = -1f;
                    break;
                case 180:
                    forward = 0f;
                    strafe = -1f;
                    break;
                case 225:
                    forward = strafe = -1f;
                    break;
                case 270:
                    forward = -1f;
                    strafe = 0f;
                    break;
                case 315:
                    forward = -1f;
                    strafe = 1f;
                    break;
            }
        } else {
            forward = strafe = 0f;
        }

    }


}
