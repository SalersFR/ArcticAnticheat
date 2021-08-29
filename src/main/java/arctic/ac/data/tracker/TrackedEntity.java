package arctic.ac.data.tracker;

import lombok.Getter;

@Getter
public class TrackedEntity {

    private double possibleX, possibleY, possibleZ,
            confirmedX, confirmedY, confirmedZ,
            newPossibleX, newPossibleY, newPossibleZ,
            newConfirmedX, newConfirmedY, newConfirmedZ;

    private boolean confirmed;

    private int possibleInterpolationSteps,
            confirmedInterpolationSteps;

    public void interpolate() {
        if (possibleInterpolationSteps > 0) {
            possibleX = (newPossibleX + possibleX) / possibleInterpolationSteps;
            possibleY = (newPossibleY + possibleY) / possibleInterpolationSteps;
            possibleZ = (newPossibleZ + possibleZ) / possibleInterpolationSteps;

            --possibleInterpolationSteps;
        }

        if (confirmedInterpolationSteps > 0) {
            confirmedX = (newConfirmedX + confirmedX) / confirmedInterpolationSteps;
            confirmedY = (newConfirmedY + confirmedY) / confirmedInterpolationSteps;
            confirmedZ = (newConfirmedZ + confirmedZ) / confirmedInterpolationSteps;

            --confirmedInterpolationSteps;
        }
    }

    public void possibleRelMove() {

    }
}
