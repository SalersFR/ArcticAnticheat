package polar.ac.prediction.impl;

import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import polar.ac.data.PlayerData;
import polar.ac.event.client.MoveEvent;
import polar.ac.prediction.PredictionHandler;
import polar.ac.utils.MathHelper;
import polar.ac.utils.PLocation;
import polar.ac.utils.WorldUtils;

import java.util.Map;

public class NormalMovementPrediction extends PredictionHandler {

    final WorldUtils worldUtils = new WorldUtils();

    public NormalMovementPrediction(PlayerData data) {
        super(data);
    }

    @Override
    public void onMove(MoveEvent event) {

        PLocation movement = event.getTo().subtract(event.getFrom()); //real movement




        Vector realVectorMovement = movement.toVector();





        //last method to call

    }


    public void moveEntityWithHeading(float strafe,float forward) {

        if(worldUtils.isCollidingWithClimbable(data.getBukkitPlayerFromUUID())) {
            this.ladderMovement();
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


    }

    public void ladderMovement() {
        float f6 = 0.15F;
        predictionData.motionX = MathHelper.clamp_double(this.predictionData.motionX, (double) (-f6), (double) f6);
        predictionData.motionZ = MathHelper.clamp_double(this.predictionData.motionZ, (double) (-f6), (double) f6);
        predictionData.fallDistance = 0.0F;

        if (predictionData.motionY < -0.15D) {
            predictionData.motionY = -0.15D;
        }

        boolean flag = data.getInteractionData().isSneaking() ;

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
        this.moveFlying(predictionData., forward, 0.02F);
        //this.moveEntity(predictionData.motionX, predictionData.motionY, predictionData.motionZ);
        predictionData.motionX *= 0.5D;
        predictionData.motionY *= 0.5D;
        predictionData.motionZ *= 0.5D;
        predictionData.motionY -= 0.02D;

        if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d1, this.predictionData)) {
            this.motionY = 0.30000001192092896D;
        }
    }

    public void waterMovement() {
        Map<Enchantment, Integer> enchantmentMap = data.getBukkitPlayerFromUUID().getInventory().getBoots().getEnchantments();
        int enchantLvl;

        if (enchantmentMap.containsKey(Enchantment.DEPTH_STRIDER)) {
            enchantLvl = enchantmentMap.get(Enchantment.DEPTH_STRIDER);
        }

        double d0 = this.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) 0; // Add depth strider modifier here.

        if (f3 > 3.0F)
        {
            f3 = 3.0F;
        }

        if (data.getBukkitPlayerFromUUID().isOnGround())
        {
            f3 *= 0.5F;
        }
    }
}
