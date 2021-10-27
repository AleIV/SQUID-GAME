package me.aleiv.core.paper.map.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView.Scale;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.MapSystemManager;
import me.aleiv.core.paper.map.renderer.CustomRender;

@CommandAlias("map")
public class MapCommands extends BaseCommand {
    private MapSystemManager manager;

    public MapCommands(MapSystemManager manager) {
        this.manager = manager;
    }

    @Default
    public void creteMap(Player sender, String imageName) {
        var mapView = Bukkit.createMap(sender.getWorld());
        // Store the maps
        manager.getMap().put(mapView, sender.getUniqueId());
        // Remove all renderers
        mapView.getRenderers().forEach(renderer -> mapView.removeRenderer(renderer));
        // Disable tracking
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        mapView.setScale(Scale.FARTHEST);
        mapView.addRenderer(new CustomRender(imageName));

        var item = new ItemStack(Material.FILLED_MAP);
        var meta = item.getItemMeta();
        // Set the map to the object.
        if (meta instanceof MapMeta mapMeta) {
            mapMeta.setMapView(mapView);
        }
        item.setItemMeta(meta);
        // Give the player the map.
        sender.getInventory().addItem(item);
    }

    @Subcommand("mandar-a-la-verga")
    public void iterateThroughPixels(Player sender, int amount, int interval) {

        for (int i = 0; i < amount; i++) {
            Bukkit.getScheduler().runTaskLater(Core.getInstance(),
                    () -> manager.getMap().entrySet().stream().filter(owner -> owner.getValue() == sender.getUniqueId())
                            .forEach(map -> this.manager.updateMap(map.getKey(), sender)),
                    i * interval);

        }

        ;

    }

}
