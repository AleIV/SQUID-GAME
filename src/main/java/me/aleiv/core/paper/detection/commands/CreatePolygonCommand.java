package me.aleiv.core.paper.detection.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.detection.CollisionManager;
import me.aleiv.core.paper.detection.lib.GeoPolygon;
import me.aleiv.core.paper.detection.objects.Position;

@CommandAlias("create-polygon")
public class CreatePolygonCommand extends BaseCommand {
    private CollisionManager collisionManager;
    private HashMap<UUID, GeoPolygon> pendingPolygons = new HashMap<>();

    public CreatePolygonCommand(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    /**
     * Creates a new polygon for the player and adds the current block the player is
     * looking at to it as the first vertex.
     *
     * @param player The player who created the polygon.
     */
    @Default
    public void createPolygon(Player player) {
        var oldPolygon = pendingPolygons.get(player.getUniqueId());
        if (oldPolygon != null) {
            player.sendMessage(Core.getMiniMessage().parse(
                    "<red>Your previous polygon with " + oldPolygon.getV().size() + " vertices has been removed."));
        }
        var block = player.getTargetBlock(4);
        if (block == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>Block can't be null"));
            return;
        }
        var vertex = Position.fromLocation(block.getLocation());
        var polygon = new GeoPolygon(vertex);
        // Send message to player confirming the creation of the polygon
        player.sendMessage(Core.getMiniMessage().parse("<green>You have created a polygon with 1 vertex."));
        pendingPolygons.put(player.getUniqueId(), polygon);
    }

    /**
     * Adds the current block the player is looking at to the polygon.
     * 
     * @param player The player who is adding a vertex to the polygon.
     */
    @Subcommand("add-vertex")
    public void addVertex(Player player) {
        var polygon = pendingPolygons.get(player.getUniqueId());
        if (polygon == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>You don't have a polygon to add a vertex to."));
            return;
        }
        var block = player.getTargetBlock(4);
        if (block == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>Block can't be null"));
            return;
        }
        var vertex = Position.fromLocation(block.getLocation());
        polygon.getV().add(vertex.toGeoPoint());
        // Send message to player confirming the creation of the polygon
        player.sendMessage(Core.getMiniMessage()
                .parse("<green>You have added a vertex " + vertex.toString() + "to your polygon."));
    }

    @Subcommand("finish")
    public void addPolygonToCollisionCheckTask(Player player) {
        var polygon = pendingPolygons.get(player.getUniqueId());
        if (polygon == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>You don't have a polygon."));
            return;
        }
        collisionManager.addPolygon(polygon);
        player.sendMessage(Core.getMiniMessage().parse("<green>Your polygon has been added to the collision checker."));

    }

    @Subcommand("apply-velocity")
    public void moveUpwards(Player player, Double xComponent, Double yComponent, Double zComponent) {
        player.getLocation().getNearbyEntitiesByType(Boat.class, 5, 5, 5)
                .forEach(all -> all.setVelocity(new Vector(xComponent, yComponent, zComponent)));
        ;
    }

}
