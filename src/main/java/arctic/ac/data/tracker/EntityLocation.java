package arctic.ac.data.tracker;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EntityLocation {

    private double minX, minY, minZ,
            maxX, maxY, maxZ,
            newMinX, newMinY, newMinZ,
            newMaxX, newMaxY, newMaxZ;

    private int interpolationSteps;

    @Setter
    private boolean confirmed;

    private short id;

    public void spawn(double x, double y, double z) {
        this.minX = x;
        this.maxX = x;
        this.minY = y;
        this.maxY = y;
        this.minZ = z;
        this.maxZ = z;
    }

    public void expand(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;
        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;
    }

    public void move(double x, double y, double z) {
        newMinX = minX += x;
        newMinY = minY += y;
        newMinZ = minZ += z;
        newMaxX = maxX += x;
        newMaxY = maxY += y;
        newMaxZ = maxZ += z;

        interpolationSteps = 3;
    }

    public void teleport(double x, double y, double z) {
        newMinX = x - 0.4D;
        newMinY = y - 0.1D; // Am stupid not sure if expand bottom
        newMinZ = z - 0.4D;
        newMaxX = x + 0.4D;
        newMaxY = y + 1.9D;
        newMaxZ = z + 0.4D;

        interpolationSteps = 3;
    }

    public void interpolate() {
        if (interpolationSteps > 3) {
            minX = (newMinX - minX) / interpolationSteps;
            minY = (newMinY - minY) / interpolationSteps;
            minZ = (newMinZ - minZ) / interpolationSteps;

            maxX = (newMaxX - maxX) / interpolationSteps;
            maxY = (newMaxY - maxY) / interpolationSteps;
            maxZ = (newMaxZ - maxZ) / interpolationSteps;

            --interpolationSteps;
        }
    }
}
