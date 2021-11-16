package us.jcedeno.cookie.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import lombok.Getter;

/**
 * An object which represents an abstract MapView and Canvas that get's updated
 * asyncrhonously. Each CookieMap should contain a MapView, a MapId, and other
 * metadata about the map and the owner of the map.
 * 
 * @author jcedeno
 */
public class CookieMap {

    /** MapView used by bukkit to handle all the logic of the bukkit map */
    private @Getter MapView mapView;
    /** A Map Id obtained from MapView */
    private final @Getter Integer mapId;
    /** Renderer used to draw the pixels of the images that we use. */
    private @Getter CustomRender mapRenderer;
    /** The type of the current cookie. */
    private @Getter CookieEnum cookieType;

    /**
     * Constructor for the CookieMap object.
     * 
     * @param mapView The MapView object that will be used to render the map.
     */
    public CookieMap(World world, CookieEnum cookieType) {
        /** Create the bukkit map and initialize everything */
        this.mapView = Bukkit.createMap(world);
        this.mapId = mapView.getId();
        this.cookieType = cookieType;

        /** Intialize map with all the important stuff. */
        this.mapView.getRenderers().forEach(mapView::removeRenderer);
        /** Add a custom renderer from the asset location. */
        this.mapView.addRenderer(CustomRender.fromFile(cookieType.getAssetLocation()));
        /** boiler-plate */
        this.mapView.setTrackingPosition(false);
        this.mapView.setUnlimitedTracking(false);
        this.mapView.setScale(Scale.FARTHEST);
    }

    /**
     * @return A new ItemStack with this map as the item.
     */
    public ItemStack getMapAsItem() {
        /** Create map item and clone meta */
        final var item = new ItemStack(Material.FILLED_MAP);
        final var meta = item.getItemMeta();
        /** This if statement should always be true, it's more like a secure cast. */
        if (meta instanceof MapMeta mapMeta)
            mapMeta.setMapView(mapView);
        /** Set the new meta */
        item.setItemMeta(meta);

        return item;
    }

}
