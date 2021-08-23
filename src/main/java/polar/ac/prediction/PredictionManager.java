package polar.ac.prediction;

import lombok.Getter;
import polar.ac.data.PlayerData;
import polar.ac.data.impl.PredictionData;
import polar.ac.prediction.impl.NormalMovementPrediction;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PredictionManager {

    private List<PredictionHandler> predictions = new ArrayList<>();

    public PredictionManager(PlayerData data) {
        predictions.add(new NormalMovementPrediction(data));

    }
}
