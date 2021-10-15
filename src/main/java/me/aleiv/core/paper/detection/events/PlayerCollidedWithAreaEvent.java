package me.aleiv.core.paper.detection.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.aleiv.core.paper.detection.lib.GeoPolygon;

/**
 * Event that is fired when a player collides with an area.
 * 
 * @author jcedeno
 */
public class PlayerCollidedWithAreaEvent extends Event {
    // Some code required for bukkit to recognize this event
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({ "java:S116", "java:S1170" })
    private final @Getter HandlerList Handlers = HandlerList;
    // Variables
    private GeoPolygon polygon;
    private UUID playerId;

    /**
     * Constructor.
     * 
     * @param polygon  The polygon that was collided with.
     * @param playerId The player that collided with the polygon.
     * @param async    Whether or not the event should be handled asynchronously.
     */
    public PlayerCollidedWithAreaEvent(GeoPolygon polygon, UUID playerId, boolean async) {
        super(async);
        this.polygon = polygon;
        this.playerId = playerId;
    }

    /**
     * Get the polygon that was collided with.
     * 
     * @return The polygon that was collided with.
     */
    public GeoPolygon getPolygon() {
        return polygon;
    }

    /**
     * Get the player that collided with the polygon.
     * 
     * @return The player that collided with the polygon or null.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    /**
     * Get the player's UUID.
     * 
     * @return The player's UUID.
     */
    public UUID getPlayerId() {
        return playerId;
    }
}
