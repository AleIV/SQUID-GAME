package me.aleiv.core.paper.detection;

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
    private Thread collisionLogicThread;
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
        this.collisionLogicThread = new Thread(this.collisionLogicTask);
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

    /**
     * Method that must be called to start the detection thread.
     */
    public void start() {
        this.collisionLogicThread.start();
    }

    /**
     * Method that must be called to stop the detection thread.
     */
    public void stop() {
        this.collisionLogicThread.interrupt();
    }

}
