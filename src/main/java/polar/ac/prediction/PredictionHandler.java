package polar.ac.prediction;

import polar.ac.data.PlayerData;
import polar.ac.data.impl.PredictionData;
import polar.ac.event.client.MoveEvent;

public abstract class PredictionHandler {

    public abstract void onMove(MoveEvent event);

    protected PlayerData data;
    protected PredictionData predictionData;

    public double posX, posY, posZ;

    public PredictionHandler(PlayerData data) {
        this.data = data;
        if (data != null) {
            this.predictionData = data.getPredictionData();

            if (data.getLocation() != null) {
                this.posX = data.getLocation().getX();
                this.posY = data.getLocation().getY();
                this.posZ = data.getLocation().getZ();
            }

        }


    }
}