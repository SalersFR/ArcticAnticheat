package dev.arctic.anticheat.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Getter
@Setter
@AllArgsConstructor
public class ArcticLocation {

    private double x,y,z;

    public Vector toBukkitVec() {
        return new Vector(x,y,z);
    }

    public Location toLoc(final World world) {
        return toBukkitVec().toLocation(world);
    }




}
