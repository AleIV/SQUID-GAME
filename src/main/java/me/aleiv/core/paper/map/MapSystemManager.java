package me.aleiv.core.paper.map;

import org.bukkit.Bukkit;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.listener.MapListener;

/**
 * A class that manages the map system. In is intended to be used to handle the
 * abstractions of translating a pixel point from the player's view vector in to
 * a pixel int the AB plane of the map the player is pointing act.
 * 
 * @author jcedeno
 */
public class MapSystemManager {
    private Core instance;
    private MapListener mapListener;

    /**
     * Constructor.
     * 
     * @param instance The instance of the core.
     */
    public MapSystemManager(Core instance) {
        this.instance = instance;
        this.mapListener = new MapListener(this);
        Bukkit.getPluginManager().registerEvents(this.mapListener, this.instance);
    }

    // Create a map and give to player.
    public void createMap(String name, int width, int height) {
    }

}
