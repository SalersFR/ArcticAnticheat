package polar.ac.prediction.impl;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import polar.ac.data.PlayerData;
import polar.ac.data.impl.PredictionData;
import polar.ac.event.client.MoveEvent;
import polar.ac.prediction.PredictionHandler;
import polar.ac.utils.*;

import java.util.Map;

public class NormalMovementPrediction extends PredictionHandler {

    final WorldUtils worldUtils = new WorldUtils();

    public NormalMovementPrediction(PlayerData data) {
        super(data);
    }

    @Override
    public void onMove(MoveEvent event) {

        PLocation movement = event.getTo().subtract(event.getFrom()); //real movement

        forwardAndStrafeHandling(event, this.predictionData);


        Vector realVectorMovement = movement.toVector();

        moveEntityWithHeading(predictionData.strafe, predictionData.forward);

        final double deltaXZ = event.getDeltaXZ();
        final double motionXZ = Math.sqrt((predictionData.motionX * predictionData.motionX) + (predictionData.motionZ * predictionData.motionZ));

        Bukkit.broadcastMessage("expected=" + motionXZ +"\nreal=" + deltaXZ);






    }


    public void moveEntityWithHeading(float strafe, float forward) {
        EntityPlayer player = ((CraftPlayer) data.getPlayer()).getHandle();

        if(!predictionData.isLava && !predictionData.isWater) {

            float f4 = 0.91F;

            if (predictionData.isGround) {
                f4 = getFrictionNMS(player, data.getPlayer().getLocation()) * 0.91F;
            }

            float f = 0.16277136F / (f4 * f4 * f4);
            float f5;

            if (predictionData.isGround) {
                f5 = this.landMovementFactor() * f;
            } else {
                f5 = this.jumpMovementFactor();
            }

            this.moveFlying(strafe, forward, f5);
            f4 = 0.91F;


            if (predictionData.isGround) {
                f4 = getFrictionNMS(player, data.getPlayer().getLocation()) * 0.91F;
            }

            predictionData.motionX *= f4;
            predictionData.motionY *= 0.98F;
            predictionData.motionZ *= f4;

        }
        if (worldUtils.isCollidingWithClimbable(data.getBukkitPlayerFromUUID())) {
            this.ladderMovement();
        }

        if (predictionData.isLadder) {
            this.ladderMovement();
        }

        if (predictionData.isWater) {
            this.waterMovement();
        }

        if (predictionData.isLava) {
            //this.ladderMovement();
        }




        this.endOfTick();

    }

    /**
     * this method is called at the last position of all predictions methods here
     */

    public void endOfTick() {



        //Gravity handling
        predictionData.motionY -= 0.08F;
        predictionData.motionY *= 0.98F;

        if (Math.abs(predictionData.motionX) < 0.005D) {
            predictionData.motionX = 0.0D;
        }

        if (Math.abs(predictionData.motionY) < 0.005D) {
            predictionData.motionY = 0.0D;
        }

        if (Math.abs(predictionData.motionZ) < 0.005D) {
            predictionData.motionZ = 0.0D;
        }


    }

    /**
     * called when player is on ladder
     */
    public void ladderMovement() {
        float f6 = 0.15F;
        predictionData.motionX = MathHelper.clamp_double(this.predictionData.motionX, (double) (-f6), (double) f6);
        predictionData.motionZ = MathHelper.clamp_double(this.predictionData.motionZ, (double) (-f6), (double) f6);
        predictionData.fallDistance = 0.0F;

        if (predictionData.motionY < -0.15D) {
            predictionData.motionY = -0.15D;
        }

        boolean flag = data.getInteractionData().isSneaking();

        if (flag && predictionData.motionY < 0.0D) {
            predictionData.motionY = 0.0D;
        }

    }

    public void moveFlying(float strafe, float forward, float friction) {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);

            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(predictionData.rotationYaw * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(predictionData.rotationYaw * (float) Math.PI / 180.0F);
            predictionData.motionX += (double) (strafe * f2 - forward * f1);
            predictionData.motionZ += (double) (forward * f2 + strafe * f1);
        }
    }

    public void lavaMovement() {
        double d1 = this.posY;
        this.moveFlying(predictionData.strafe, predictionData.forward, 0.02F);
        //this.moveEntity(predictionData.motionX, predictionData.motionY, predictionData.motionZ);
        predictionData.motionX *= 0.5D;
        predictionData.motionY *= 0.5D;
        predictionData.motionZ *= 0.5D;
        predictionData.motionY -= 0.02D;
        /**
        if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(predictionData.motionX, predictionData.motionY
        + 0.6000000238418579D - this.posY + d1, this.predictionData)) {
        predictionData.motionY = 0.30000001192092896D;
        }
     **/
    }

    public void waterMovement() {
        Map<Enchantment, Integer> enchantmentMap = data.getBukkitPlayerFromUUID().getInventory().getBoots().getEnchantments();
        int enchantLvl = 0;

        if (enchantmentMap.containsKey(Enchantment.DEPTH_STRIDER)) {
            enchantLvl = enchantmentMap.get(Enchantment.DEPTH_STRIDER);
        }

        double d0 = this.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = enchantLvl; // Add depth strider modifier here.

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (data.getBukkitPlayerFromUUID().isOnGround()) {
            f3 *= 0.5F;
        }
    }

    public void forwardAndStrafeHandling(MoveEvent event, PredictionData data) {

        //CREDITS TO AAL (ORRESSS)


        PLocation to = event.getTo();
        PLocation from = event.getFrom();
        final double moveX = to.getX() - from.getX() - data.dDeltaX;
        final double moveZ = to.getZ() - from.getZ() - data.dDeltaZ;
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double y = 0; y <= 1; y++) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    String block = from.toVector().toLocation(data.getData().getPlayer().getWorld()).toString().toLowerCase();
                    data.isWater = data.isWater || block.contains("water");
                    data.isLava = data.isLava || block.contains("lava");
                    data.isWeb = data.isWeb || block.contains("web");
                }
            }
            data.dDeltaX = (to.getX() - from.getX()) * (data.isLava ? 0.5 : event.isGround() ?
                    CustomUtils.getFriction(from.toVector().toLocation(data.getData().getPlayer().getWorld())) : 0.91f);
            data.dDeltaZ = (to.getZ() - from.getZ()) * (data.isLava ? 0.5 : event.isGround() ?
                    CustomUtils.getFriction(from.toVector().toLocation(data.getData().getPlayer().getWorld())) : 0.91f);
            final double move = Math.hypot(moveX, moveZ);
            if (move > 0.01) {
                double d = Double.MAX_VALUE;
                int f = 0;
                for (int yaw = 0; yaw < 360; yaw += 45) {
                    double x2 = Math.cos((to.getYaw() + yaw) * Math.PI / 180) * move;
                    double z = Math.sin((to.getYaw() + yaw) * Math.PI / 180) * move;
                    double diff = Math.sqrt((moveX - x2) * (moveX - x2) + (moveZ - z) * (moveZ - z));
                    if (diff < d) {
                        d = diff;
                        f = yaw;
                    }
                }
                switch (f) {
                    case 0:
                        data.forward = 0f;
                        data.strafe = 1f;
                        break;
                    case 45:
                        data.forward = data.strafe = 1f;
                        break;
                    case 90:
                        data.forward = 1f;
                        data.strafe = 0f;
                        break;
                    case 135:
                        data.forward = 1f;
                        data.strafe = -1f;
                        break;
                    case 180:
                        data.forward = 0f;
                        data.strafe = -1f;
                        break;
                    case 225:
                        data.forward = data.strafe = -1f;
                        break;
                    case 270:
                        data.forward = -1f;
                        data.strafe = 0f;
                        break;
                    case 315:
                        data.forward = -1f;
                        data.strafe = 1f;
                        break;
                }
            } else {
                data.forward = data.strafe = 0f;
            }
        }

    }

    private float getFrictionNMS(EntityPlayer player, Location location) {
        return player.world.getType(
                new BlockPosition(location.getX(),
                        NumberConversions.floor(location.getY()) - 1,
                        location.getZ())
        ).getBlock().frictionFactor;
    }

    public float jumpMovementFactor() {
        double d = 0.42F;

        if(data.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
            d += (PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP)) * 0.1F;
        }

        return (float) d;


    }



    public float landMovementFactor() {

        float speed = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
        float slow = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SLOW);

        double d = 0.10000000149011612;
        d += d * 0.20000000298023224 * speed;
        d += d * -0.15000000596046448 * slow;

        // Sprint desync big gay just assume they are sprinting
        d += d * 0.30000001192092896;

        float landMovementFactor = (float) d;

        return landMovementFactor;
    }

}
