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
        return loc.getWorld() == this.world && loc.toVector().isInAABB(lower, upper);
    }

}
