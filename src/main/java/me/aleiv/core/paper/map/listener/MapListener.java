package me.aleiv.core.paper.map.listener;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.MapSystemManager;
import me.aleiv.core.paper.map.events.PlayerClicksOnMapEvent;
import me.aleiv.core.paper.map.objects.AsyncCanvas;

public class MapListener implements Listener {
    private MapSystemManager mapSystemManager;

    public MapListener(MapSystemManager manager) {
        this.mapSystemManager = manager;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();
        var entity = event.getRightClicked();

        if (entity != null && entity instanceof ItemFrame frame) {
            System.out.println("Frame part 1");
            ItemStack itemInFrame = frame.getItem();
            if (itemInFrame != null && itemInFrame.getType() == Material.FILLED_MAP
                    && itemInFrame.getItemMeta() instanceof MapMeta map) {

                System.out.println("Frame part 2");
                Bukkit.getPluginManager().callEvent(new PlayerClicksOnMapEvent(player, event,
                        AsyncCanvas.of(map.getMapView()), !Bukkit.isPrimaryThread()));
            }

        }

    }

    @EventHandler
    public void clickMapCancel(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() != null && e.getRightClicked() instanceof ItemFrame) {
            if (this.mapSystemManager.allowedRotation)
                e.setCancelled(true);
        }
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
        var canvas = e.getAsyncCanvas();

        // Calculate the relative pixel values
        var position = e.getClickedPosition().toBlockVector();
        var block = e.getBlock();
        System.out.println(position);

        // TODO: Make this work any plane.
        // Assume we are on a xz plane, multiply by 256 and substract 128 to map it.
        var adjustedX = Math.round((position.getX() + 0.5) * 128);
        var adjustedZ = Math.round((position.getZ() + 0.5) * 128);
        e.getPlayer().sendMessage((int) adjustedX + ", " + (int) adjustedZ);

        try {
            this.mapSystemManager.getProtocolManager().sendServerPacket(e.getPlayer(),
                    canvas.updateMapPixel((int) adjustedX, (int) adjustedZ));
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }

        // Now somehow get the map involved and render the pixel onto it.

    }
}
