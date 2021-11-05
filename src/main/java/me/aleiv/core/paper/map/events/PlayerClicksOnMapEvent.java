package me.aleiv.core.paper.map.events;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import lombok.Getter;
import me.aleiv.core.paper.map.objects.AsyncCanvas;

/**
 * 
 * A custom event to be called when a player clicks on a map canvas.
 * 
 * The {@link #triggedByEvent} object can be of types
 * {@link org.bukkit.event.player.PlayerInteractEntityEvent},
 * {@link org.bukkit.event.player.PlayerInteractEvent},
 * 
 * @author jcedeno
 */
public class PlayerClicksOnMapEvent extends Event {
    // Some code required for bukkit to recognize this event
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({ "java:S116", "java:S1170" })
    private final @Getter HandlerList Handlers = HandlerList;
    // Variables
    private Event triggedByEvent;
    private Player player;
    private Block block;
    private Vector clickedPosition;
    public @Getter AsyncCanvas asyncCanvas;
    private @Getter ItemFrame itemFrame;

    public PlayerClicksOnMapEvent(Player player, Event triggedByEvent, AsyncCanvas asyncCanvas, ItemFrame itemFrame,
            boolean isAsync) {
        super(isAsync);
        this.asyncCanvas = asyncCanvas;
        this.triggedByEvent = triggedByEvent;
        this.itemFrame = itemFrame;
        this.player = player;
        // Calculate the block and clicked positions
        if (triggedByEvent instanceof PlayerInteractAtEntityEvent e) {
            this.block = e.getRightClicked().getLocation().getBlock();
            this.clickedPosition = e.getClickedPosition();
        } else if (triggedByEvent instanceof PlayerInteractEvent e) {
            this.block = e.getClickedBlock();
            e.getBlockFace();
            this.clickedPosition = e.getInteractionPoint().toVector();
        }
    }

    /**
     * @return The player that triggered the event.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the event that triggered this event
     */
    public Event getTriggedByEvent() {
        return triggedByEvent;
    }

    /**
     * @return The block that was clicked.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * @return The position of the click.
     */
    public Vector getClickedPosition() {
        return clickedPosition;
    }

    /**
     * @return An optional containing the PlayerInteractEntityEvent, if there is
     *         one.
     */
    public Optional<PlayerInteractEntityEvent> getTriggedByPlayerInteractEntityEvent() {
        if (triggedByEvent instanceof PlayerInteractEntityEvent newEvent) {
            return Optional.of(newEvent);
        }
        return Optional.empty();
    }

    /**
     * @return An optional containing the PlayerInteractEvent, if there is one.
     */
    public Optional<PlayerInteractEvent> getTriggedByPlayerInteractEvent() {
        if (triggedByEvent instanceof PlayerInteractEvent newEvent) {
            return Optional.of(newEvent);
        }
        return Optional.empty();
    }

}
