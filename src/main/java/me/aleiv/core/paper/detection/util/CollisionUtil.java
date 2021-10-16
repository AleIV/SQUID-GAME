package me.aleiv.core.paper.detection.util;

import org.bukkit.entity.Player;

import me.aleiv.core.paper.detection.lib.GeoPolygon;
import me.aleiv.core.paper.detection.objects.Position;

/**
 * A utility class for collision detection of players.
 * 
 * 
 * @author jcedeno
 */
public class CollisionUtil {

    /**
     * Checks if a player is inside a polygon.
     * 
     * @param polygon The polygon to check.
     * @param world   The world of the polygon.
     * @param player  The player to check.
     * @return True if the player is inside the polygon, false otherwise.
     */
    public static boolean isPlayerInsidePolygon(GeoPolygon polygon, String world, Player player) {
        return player.getWorld().getName().equalsIgnoreCase(world)
                && polygon.isInside(Position.fromLocation(player.getLocation()));
    }

}
