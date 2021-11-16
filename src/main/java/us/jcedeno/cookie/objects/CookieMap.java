package us.jcedeno.cookie.objects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.map.MapView;

import lombok.Getter;
import me.aleiv.core.paper.map.renderer.CustomRender;

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
        this.mapView = mapView;
        this.mapId = mapView.getId();
        this.cookieType = cookieType;

        this.cookieType.getAssetLocation();

    }

}
