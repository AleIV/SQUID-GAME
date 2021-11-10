package me.aleiv.core.paper.map.listener;

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
import me.aleiv.core.paper.map.packet.WrapperPlayServerMap;
import me.aleiv.core.paper.utilities.LineVector;

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
            ItemStack itemInFrame = frame.getItem();
            if (itemInFrame != null && itemInFrame.getType() == Material.FILLED_MAP
                    && itemInFrame.getItemMeta() instanceof MapMeta map) {
                var view = map.getMapView();

                var owner = this.mapSystemManager.getOwnerOfCanvas(view);

                if (owner != null && owner.getValue() == player.getUniqueId()) {
                    Bukkit.getPluginManager().callEvent(new PlayerClicksOnMapEvent(player, event, owner.getKey(), frame,
                            !Bukkit.isPrimaryThread()));
                }

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
        var block = interactionPoint.getBlock();
        var itemFrameIter = block.getLocation().add(0.5, 0, 0.5).getNearbyEntitiesByType(ItemFrame.class, 0.25)
                .iterator();
        if (itemFrameIter.hasNext()) {
            var itemFrame = itemFrameIter.next();

            ItemStack itemInFrame = itemFrame.getItem();
            if (itemInFrame != null && itemInFrame.getType() == Material.FILLED_MAP
                    && itemInFrame.getItemMeta() instanceof MapMeta map) {

                var view = map.getMapView();

                var owner = this.mapSystemManager.getOwnerOfCanvas(view);

                if (owner != null && owner.getValue() == player.getUniqueId()) {

                    var block2 = itemFrame.getLocation().toBlockLocation();
                    var relativeX = (block2.getX() + 1) - interactionPoint.getX();
                    var relativeZ = (block2.getZ() + 1) - interactionPoint.getZ();
                    AsyncCanvas c = owner.getKey();

                    // Get the location of the player's eyes/head.
                    var playerEyes = player.getEyeLocation();
                    // Draw particles to see the vector of the player's eyes.
                    LineVector.of(event.getInteractionPoint().toVector(), playerEyes.toVector()).getPointsInBetween()
                            .forEach(vectors -> {
                                var location = vectors.toLocation(player.getWorld());

                                Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                                    location.getWorld().spawnParticle(Particle.COMPOSTER, location, 1);

                                }, 100L);

                            });

                    // Currently works with Clockwise edges only
                    // Obtain the adjusted pixels.
                    var adjustedX = ((int) (127 * relativeX));
                    var adjustedY = ((int) (127 - (int) (127 * relativeZ)));

                    // Draw onto canvas
                    c.updateMapPixel(adjustedX, adjustedY);

                    Bukkit.getPluginManager().callEvent(new PlayerClicksOnMapEvent(player, event, owner.getKey(),
                            itemFrame, !Bukkit.isPrimaryThread()));

                }

            }

        }
    }

    public void createMap(World world) {
        Bukkit.createMap(world);
    }

    @EventHandler
    public void clickOnMap(PlayerClicksOnMapEvent e) {
        var canvas = e.getAsyncCanvas();

        var frame = e.getItemFrame();

        int x = 0, z = 0;
        var position = e.getClickedPosition().toBlockVector();

        WrapperPlayServerMap packet = null;
        if (e.getTriggedByPlayerInteracAtEntityEvent().isPresent()) {

            switch (frame.getRotation()) {

            case NONE:
            case FLIPPED: {
                x = (int) Math.round((position.getX() + 0.5) * 128);
                z = (int) Math.round((position.getZ() + 0.5) * 128);
                packet = canvas.updateMapPixel(x, z);
                break;
            }
            case CLOCKWISE_45:
            case FLIPPED_45:

                z = (int) Math.round((position.getX() + 0.5) * 128);
                x = (int) Math.round((position.getZ() + 0.5) * 128);
                packet = canvas.updateMapPixel(x, 128 - z);

                break;
            case COUNTER_CLOCKWISE:
            case CLOCKWISE:

                x = (int) Math.round((position.getX() + 0.5) * 128);
                z = (int) Math.round((position.getZ() + 0.5) * 128);
                packet = canvas.updateMapPixel(128 - x, 128 - z);

                break;
            case COUNTER_CLOCKWISE_45:
            case CLOCKWISE_135:

                z = (int) Math.round((position.getX() + 0.5) * 128);
                x = (int) Math.round((position.getZ() + 0.5) * 128);
                packet = canvas.updateMapPixel(128 - x, z);

                break;
            }
        } else {
            switch (frame.getRotation()) {

            case NONE:
            case FLIPPED: {
                x = (int) Math.ceil((position.getX()) * 128) / 2;
                z = (int) Math.ceil((position.getZ()) * 128) / 2;
                packet = canvas.updateMapPixel(Math.min(127, x), Math.min(127, z));
                break;
            }
            case CLOCKWISE_45:
            case FLIPPED_45:

                z = (int) Math.ceil((position.getX()) * 128) / 2;
                x = (int) Math.ceil((position.getZ()) * 128) / 2;
                packet = canvas.updateMapPixel(Math.min(x, 127), Math.min(128 - z, 127));

                break;
            case COUNTER_CLOCKWISE:
            case CLOCKWISE:

                x = (int) Math.ceil((position.getX()) * 128) / 2;
                z = (int) Math.ceil((position.getZ()) * 128) / 2;
                packet = canvas.updateMapPixel(Math.min(128 - x, 127), Math.min(128 - z, 127));

                break;
            case COUNTER_CLOCKWISE_45:
            case CLOCKWISE_135:

                z = (int) Math.ceil((position.getX()) * 128) / 2;
                x = (int) Math.ceil((position.getZ()) * 128) / 2;
                packet = canvas.updateMapPixel(Math.min(128 - x, 127), Math.min(127, z));

                break;
            }

        }
        if (packet != null)
            packet.broadcastPacket();

        // Now somehow get the map involved and render the pixel onto it.

    }
}
