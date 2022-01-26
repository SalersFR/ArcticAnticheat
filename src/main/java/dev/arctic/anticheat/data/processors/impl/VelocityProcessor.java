package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.ArcticLocation;
import dev.arctic.anticheat.utilities.PlayerUtils;
import lombok.Getter;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class VelocityProcessor extends Processor {

    public double velocityX, velocityY, velocityZ, predictedVelocityY, predictedVelocityH;
    public int velocityH, velocityV;
    public int velocityTicks;
    private Vector velocity = new Vector();

    public VelocityProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {
            if(velocityV > 0) {
                velocityV--;
            } else {
                predictedVelocityY = 0;
            }

            if(velocityH > 0) {
                velocityH--;
            }

            if(velocityTicks > 0) {
                if(predictedVelocityY > 0) {
                    predictedVelocityY -= 0.08D;
                    predictedVelocityY *= 0.98F;
                } else predictedVelocityY = 0;
            }

            if(data.getMovementProcessor().getDeltaY() == 0.42F) {
                predictedVelocityY = 0;
            }

            if(predictedVelocityY < 0.005) {
                predictedVelocityY = 0;
            }

            ++velocityTicks;

            double moveFlying = movingFlying(data);

            if(data.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                int amplifier = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
                predictedVelocityH = (predictedVelocityH * Math.pow(0.9, amplifier)) - 0.01;
            }

            if(velocityTicks == 1) {
                if(!data.getCollisionProcessor().isClientOnGround() && data.getCollisionProcessor().isLastClientOnGround()) {
                    predictedVelocityH = predictedVelocityH - moveFlying;
                } else {
                    predictedVelocityH = predictedVelocityH * 0.91 - moveFlying;
                }
            } else if(velocityTicks == 2 && predictedVelocityH > 0) {
                if(data.getCollisionProcessor().isLastLastClientOnGround()) {
                    predictedVelocityH = predictedVelocityH * 0.6 * 0.91 - moveFlying;
                } else {
                    predictedVelocityH = predictedVelocityH * 0.91 - moveFlying;
                }
            } else if(velocityTicks > 2 && velocityTicks <= 10 && predictedVelocityH > 0) {
                predictedVelocityH = predictedVelocityH * 0.91 - moveFlying;
            }

            predictedVelocityH = Math.max(predictedVelocityH, 0);

            if(data.getMovementProcessor().getDeltaY() == 0.42F || predictedVelocityH < 0.005) {
                predictedVelocityH = 0;
            }
        } else if(event.getPacket().isUseEntity()) {
            velocity.setX(velocity.getX() * 0.6F);
            velocity.setZ(velocity.getZ() * 0.6F);
            predictedVelocityH *= 0.6F;
        }
    }

    @Override
    public void handleSending(PacketEvent event) {
        if(event.getPacket().isVelocity()) {
            final WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(event.getPacket());

            if(packet.getEntityID() == data.getPlayer().getEntityId()) {
                final double x = packet.getVelocityX();
                final double y = packet.getVelocityY();
                final double z = packet.getVelocityZ();
                velocity = new Vector(x, y, z);

                data.getTransactionProcessor().todoTransaction(() -> {
                    velocityX = velocity.getX();
                    velocityY = velocity.getY();
                    velocityZ = velocity.getZ();

                    velocityH = (int) (((Math.abs(velocityX) + Math.abs(velocityZ)) / 2.0 + 2.0) * 15.0);
                    velocityV = (int) (Math.pow(velocity.getY() + 2.0, 2.0) * 5.0);

                    predictedVelocityH = Math.hypot(velocity.getX(), velocity.getZ());
                    predictedVelocityY = velocity.getY();

                    velocityTicks = 0;
                });

            }

        }

    }

    public static double movingFlying(PlayerData data) {
        ArcticLocation to = data.getMovementProcessor().getLocation(), from = data.getMovementProcessor().getLastLocation();

        double preD = 0.01D;

        double mx = to.getX() - from.getX();
        double mz = to.getZ() - from.getZ();

        float motionYaw = (float) (Math.atan2(mz, mx) * 180.0D / Math.PI) - 90.0F;

        int direction;

        motionYaw -= data.getRotationProcessor().getYaw();

        while (motionYaw > 360.0F)
            motionYaw -= 360.0F;
        while (motionYaw < 0.0F)
            motionYaw += 360.0F;

        motionYaw /= 45.0F;

        float moveS = 0.0F;
        float moveF = 0.0F;

        if(Math.abs(Math.abs(mx) + Math.abs(mz)) > preD) {
            direction = (int) new BigDecimal(motionYaw).setScale(1, RoundingMode.HALF_UP).doubleValue();

            if (direction == 1) {
                moveF = 1F;
                moveS = -1F;
            } else if (direction == 2) {
                moveS = -1F;
            } else if (direction == 3) {
                moveF = -1F;
                moveS = -1F;
            } else if (direction == 4) {
                moveF = -1F;
            } else if (direction == 5) {
                moveF = -1F;
                moveS = 1F;
            } else if (direction == 6) {
                moveS = 1F;
            } else if (direction == 7) {
                moveF = 1F;
                moveS = 1F;
            } else if (direction == 8) {
                moveF = 1F;
            } else if (direction == 0) {
                moveF = 1F;
            }
        }

        moveS *= 0.98F;
        moveF *= 0.98F;

        float strafe = moveS, forward = moveF;
        float f = strafe * strafe + forward * forward;

        float friction;

        float var3 = (0.6F * 0.91F);
        float getAIMoveSpeed = 0.13000001F;

        if(data.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            switch(PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED)) {
                case 0: {
                    getAIMoveSpeed = 0.23400002F;
                    break;
                }

                case 1: {
                    getAIMoveSpeed = 0.156F;
                    break;
                }

                case 2: {
                    getAIMoveSpeed = 0.18200001F;
                    break;
                }

                case 3: {
                    getAIMoveSpeed = 0.208F;
                    break;
                }

                case 4: {
                    getAIMoveSpeed = 0.23400001F;
                    break;
                }

                default: {
                    break;
                }
            }
        }

        float var4 = 0.16277136F / (var3 * var3 * var3);

        if(data.getCollisionProcessor().isLastClientOnGround()) {
            friction = getAIMoveSpeed * var4;
        } else {
            friction = 0.026F;
        }

        if(f >= 1.0E-4F) {
            f = (float) Math.sqrt(f);
            if (f < 1.0F) {
                f = 1.0F;
            }
            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = (float) Math.sin(data.getRotationProcessor().getYaw() * (float) Math.PI / 180.0F);
            float f2 = (float) Math.cos(data.getRotationProcessor().getYaw() * (float) Math.PI / 180.0F);
            float motionXAdd = (strafe * f2 - forward * f1);
            float motionZAdd = (forward * f2 + strafe * f1);
            return Math.hypot(motionXAdd, motionZAdd);
        }

        return 0;
    }

}
