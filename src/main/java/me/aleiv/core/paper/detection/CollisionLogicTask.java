package me.aleiv.core.paper.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.detection.events.PlayerCollidedWithAreaEvent;
import me.aleiv.core.paper.detection.lib.GeoPolygon;
import me.aleiv.core.paper.detection.objects.Position;

/**
 * A java runnable that calculates when players collide with geometric zones on
 * a bukkit world. It should trigger a bukkit event to noti
 * 
 * @author jcedeno
 */
public class CollisionLogicTask implements Runnable {
    private Core core;
    private List<GeoPolygon> polygons;

    public CollisionLogicTask(Core core) {
        this.core = core;
        this.polygons = new ArrayList<>();
    }

    @Override
    public void run() {
        // If no ploygons are loaded, don't do anything.
        if (polygons.isEmpty())
            return;

        final var locationsMap = getPlayersLocations();
        // If no players are online, don't do anything.
        if (locationsMap.isEmpty())
            return;

        final var entries = locationsMap.entrySet();

        for (final var entry : entries) {
            final var playerUUID = entry.getKey();
            final var playerPosition = entry.getValue();

            for (final var polygon : polygons) {
                if (polygon.isInside(playerPosition)) {
                    final var event = new PlayerCollidedWithAreaEvent(polygon, playerUUID, !Bukkit.isPrimaryThread());
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }
    }

    /**
     * Adds a polygon for the task to check on every tick.
     * 
     * @param polygon The polygon to add.
     */
    public void addPolygon(GeoPolygon polygon) {
        polygons.add(polygon);
    }

    /**
     * Obtains the current locations of all players in the the server.
     * 
     * @return a map with all player's uuids and positions.
     */
    public ConcurrentHashMap<UUID, Position> getPlayersLocations() {
        var positionsMaps = new ConcurrentHashMap<UUID, Position>();

        Bukkit.getOnlinePlayers().parallelStream()
                .forEach(p -> positionsMaps.put(p.getUniqueId(), Position.fromLocation(p.getLocation())));

        return positionsMaps;
    }

}
