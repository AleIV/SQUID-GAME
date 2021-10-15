package me.aleiv.core.paper.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import me.aleiv.core.paper.Core;
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

    private static long delay = 50L;

    public CollisionLogicTask(Core core) {
        this.core = core;
        this.polygons = new ArrayList<>();
    }

    @Override
    public void run() {
        // Continue if the task is the thread is not intterupted.
        while (!Thread.currentThread().isInterrupted()) {
            // If no ploygons are loaded, don't do anything.
            if (polygons.isEmpty())
                return;

            final var locationsMap = getPlayersLocations();
            // If no players are online, don't do anything.
            if (locationsMap.isEmpty())
                return;

            final var entries = locationsMap.entrySet();
            polygons.parallelStream().forEach(polygon -> {
                entries.parallelStream().filter(prd -> polygon.isInside(prd.getValue())).forEach(playersInside -> {
                    // Call event to bukkit

                });

            });
            // Sleep for a while.
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
