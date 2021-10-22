package me.aleiv.core.paper.detection.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.aleiv.core.paper.detection.CollisionManager;
import me.aleiv.core.paper.detection.events.PlayerEnterPolygonEvent;
import me.aleiv.core.paper.detection.events.PlayerExitPloygonEvent;
import me.aleiv.core.paper.detection.objects.Polygon;

public class CheckCollisionsTask implements Runnable {
    private CollisionManager collisionManager;
    private List<Polygon> polygonList;
    private Map<UUID, Polygon> playerPolygonMap;

    public CheckCollisionsTask(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.polygonList = new ArrayList<>();
        this.playerPolygonMap = new HashMap<>();
    }

    @Override
    public void run() {
        // Check if any of the players online is inside of any of the polygons.
        for (Player player : Bukkit.getOnlinePlayers()) {
            // If player is contained on the map already, check if he is still inside of the
            // polygon.
            var playerPolygon = playerPolygonMap.get(player.getUniqueId());
            // If player polygon is not null, then player might be entering another polygon
            // or leaving one.
            if (playerPolygon != null) {
                boolean matched = false;
                for (Polygon polygon : polygonList) {
                    if (polygon.isInside(player.getLocation())) {
                        if (!playerPolygon.equals(polygon)) {
                            // Player is leaving the old polygon
                            callExitedPolygon(player, playerPolygon);
                        }
                        // Player is entering a new polygon.
                        callEnteredPolygon(player, polygon);
                        playerPolygonMap.put(player.getUniqueId(), polygon);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    // Player is no longer inside any polygon.
                    callExitedPolygon(player, playerPolygon);
                    playerPolygonMap.remove(player.getUniqueId());
                }
            } else {
                // If player is not already inside a polygon, then they are only capable of
                // entering a new polygon.
                for (Polygon polygon : polygonList) {
                    if (polygon.isInside(player.getLocation())) {
                        callEnteredPolygon(player, polygon);
                        playerPolygonMap.put(player.getUniqueId(), polygon);
                        break;
                    }
                }
            }
        }
    }

    private void callExitedPolygon(Player player, Polygon polygon) {
        Bukkit.getPluginManager().callEvent(new PlayerExitPloygonEvent(player, polygon, !Bukkit.isPrimaryThread()));
    }

    private void callEnteredPolygon(Player player, Polygon polygon) {
        Bukkit.getPluginManager().callEvent(new PlayerEnterPolygonEvent(player, polygon, !Bukkit.isPrimaryThread()));
    }

}
