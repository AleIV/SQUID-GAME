package me.aleiv.core.paper.map;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursor.Type;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * A class that manages the map system. In is intended to be used to handle the
 * abstractions of translating a pixel point from the player's view vector in to
 * a pixel int the AB plane of the map the player is pointing act.
 * 
 * @author jcedeno
 */
public class OldMapManager implements Listener {

    private MapCursor cursor;
    private MapCanvas canvas;

    @EventHandler
    public void onPlayerInteractWithMap(PlayerInteractEntityEvent e) {

        // Check if the interacted entity is an item frame containing a map
        var entity = e.getRightClicked();
        if (entity != null && entity instanceof ItemFrame frame) {
            var map = frame.getItem();
            if (map != null && (map.getType() == Material.MAP || map.getType() == Material.FILLED_MAP)) {
                e.setCancelled(true);
                var meta = map.getItemMeta();

                if (meta instanceof MapMeta mapMeta) {
                    var player = e.getPlayer();

                    MapView mapView = mapMeta.getMapView();
                    var renderer = mapView.getRenderers().get(0);

                    var rt = player.rayTraceBlocks(3);
                    if (rt != null) {

                        var hitPositionVector = rt.getHitPosition();

                        new ParticleBuilder(Particle.WATER_DROP)
                                .location(hitPositionVector.toLocation(player.getWorld())).receivers(20).force(true)
                                .count(10).spawn();

                        player.sendMessage(MiniMessage.get().parse("<green>" + hitPositionVector));

                        // Calculate the pixel point in the map
                        var hitBlock = rt.getHitBlock();
                        var blockX = hitBlock.getX();
                        var blockZ = hitBlock.getZ();

                        var relativePointX = Math.abs(blockX) - Math.abs(hitPositionVector.getX());
                        var relativePointZ = Math.abs(hitPositionVector.getZ()) - Math.abs(blockZ);

                        var mapWidth = 256;
                        var mapHeight = 256;

                        var mapPixelX = ((int) (relativePointX * mapWidth)) - 128;
                        var mapPixelZ = ((int) (relativePointZ * mapHeight)) - 128;

                        // Send a message with the pixels
                        player.sendMessage(
                                MiniMessage.get().parse("<green>Map pixel: <yellow>" + mapPixelX + "," + mapPixelZ));
                        // Send message with relativey point
                        player.sendMessage(MiniMessage.get()
                                .parse("<green>Relative point: <yellow>" + relativePointX + "," + relativePointZ));

                        // Set the cursor
                        cursor.setY((byte) mapPixelX);
                        cursor.setX((byte) -mapPixelZ);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onMapInitialize(MapInitializeEvent e) {
        MapView mapView = e.getMap();
        mapView.setScale(Scale.FAR);
        mapView.setUnlimitedTracking(true);
        mapView.getRenderers().clear();

        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                for (int x = -127; x < 128; x++) {
                    for (int y = -127; y < 128; y++) {
                        mapCanvas.setPixel(x, y, MapPalette.WHITE);
                    }
                }
                cursor = mapCanvas.getCursors().addCursor(0, 0, (byte) 5);
                cursor.setType(Type.RED_X);

            }
        });
    }

}
