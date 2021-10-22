package me.aleiv.core.paper.map.listener;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.MapSystemManager;
import me.aleiv.core.paper.map.events.PlayerClicksOnMapEvent;

public class MapListener implements Listener {
    private MapSystemManager mapSystemManager;

    public MapListener(MapSystemManager manager) {
        this.mapSystemManager = manager;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();
        var interactionPoint = event.getClickedPosition();

        Bukkit.getScheduler().runTask(Core.getInstance(),
                () -> player.spawnParticle(Particle.WATER_DROP, interactionPoint.toLocation(player.getWorld()), 1));

    }

    @EventHandler
    public void onPlayerIntereactAtItemFrameEvent(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var interactionPoint = event.getInteractionPoint();
        if (interactionPoint == null) {
            return;
        }

        Bukkit.getScheduler().runTask(Core.getInstance(),
                () -> player.spawnParticle(Particle.WATER_DROP, interactionPoint, 1));

    }

    public void createMap(World world) {
        Bukkit.createMap(world);
    }

    @EventHandler
    public void clickOnMap(PlayerClicksOnMapEvent e) {
        var view = e.mapView;

        // Calculate the relative pixel values
        var position = e.getClickedPosition();
        var block = e.getBlock();

        // TODO: Make this work any plane.
        // Assume we are on a xz plane, multiply by 256 and substract 128 to map it.
        var adjustedX = (max(abs(position.getX()) - abs(block.getX()), 1.0) * 256) - 128;
        var adjustedZ = (max(abs(position.getZ()) - abs(block.getZ()), 1.0) * 256) - 128;

        // Now somehow get the map involved and render the pixel onto it.

        
    }
}
