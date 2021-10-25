package me.aleiv.core.paper.detection.objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Polygon {
    Vector lower;
    Vector upper;
    World world;

    /**
     * Checks if a given location is inside the polygon.
     * 
     * @param loc The location to check.
     * @return True if the location is inside the polygon, false otherwise.
     */
    public boolean isInside(Location loc) {
        System.out.println(loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
        return loc.toVector().isInAABB(lower, upper);
    }

    //A function that checks if a vector v is inside the lower and upper vectors of the polygon, and takes into consideration negative values
    


}
