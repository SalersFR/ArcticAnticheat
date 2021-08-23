package polar.ac.data.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;
import polar.ac.data.PlayerData;

@Getter
@RequiredArgsConstructor
public class PredictionData {

    private final PlayerData data;

    public double motionX,motionY,motionZ;

    public double dDeltaX,dDeltaZ;


    public float fallDistance = 0.0F,rotationYaw,strafe,forward;
    public Vector predictedMovement;
    public boolean isLava,isWater,isLadder,isGround,isWeb;
}
