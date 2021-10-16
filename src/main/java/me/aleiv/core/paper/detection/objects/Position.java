package me.aleiv.core.paper.detection.objects;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.aleiv.core.paper.detection.lib.GeoPoint;

/**
 * Represents a position of a player in a game. Designed to detect collision
 * into zones.
 * 
 * @author jcedeno
 */
public @Data @AllArgsConstructor(staticName = "of") class Position {
    private final int x;
    private final int y;
    private final int z;
    private String world;

    public static Position at(int x, int y, int z) {
        return Position.of(x, y, z, null);
    }

    public static Position fromLocation(Location loc) {
        return Position.of(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(x, y, z);
    }

}
