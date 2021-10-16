package me.aleiv.core.paper.detection;

import org.bukkit.Bukkit;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.detection.commands.CreatePolygonCommand;
import me.aleiv.core.paper.detection.lib.GeoPolygon;

/**
 * The CollisionManager class is used to easily interact with the collision
 * system of the plugin.
 * 
 * @author jcedeno
 */
public class CollisionManager {
    private Core instance;
    private CollisionLogicTask collisionLogicTask;
    private CreatePolygonCommand polygonCommand;

    /**
     * Creates a new instance of the CollisionManager class.
     * 
     * @param instance The instance of the plugin.
     */
    public CollisionManager(Core instance) {
        this.instance = instance;
        this.collisionLogicTask = new CollisionLogicTask(instance);
        this.polygonCommand = new CreatePolygonCommand(this);
        Bukkit.getScheduler().runTaskTimer(instance, this.collisionLogicTask, 0L, 5L);
        this.instance.getCommandManager().registerCommand(this.polygonCommand);
    }

    /**
     * Adds a new polygon to the collision detection system.
     * 
     * @param polygon The polygon to add.
     */
    public void addPolygon(GeoPolygon polygon) {
        this.collisionLogicTask.addPolygon(polygon);
    }

}
