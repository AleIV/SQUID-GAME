package me.aleiv.core.paper.map.objects;

import com.comphenix.protocol.events.PacketContainer;

import org.bukkit.map.MapView;

import lombok.Getter;
import me.aleiv.core.paper.map.packet.WrapperPlayServerMap;

/**
 * A class designed to act as a wrapper for a MapView object to make it easier
 * to update it asyncrhonously and keep track of the changes that haven't been
 * committed to the actual map.
 * 
 * @author jcedeno
 */
public class AsyncCanvas {

    private final @Getter MapView mapView;

    /**
     * Creates a new AsyncCanvas object.
     * 
     * @param mapView The map view to wrap.
     */
    public AsyncCanvas(MapView mapView) {
        this.mapView = mapView;
    }

    /**
     * Static constructor for a new AsyncCanvas object.
     * 
     * @param mapView the MapView object to wrap.
     * @return a new AsyncCanvas object.
     */
    public static AsyncCanvas of(MapView mapView) {
        return new AsyncCanvas(mapView);
    }

    /**
     * A method that returns a packet containing the pixel change
     * 
     * @param x The x coordinate of the pixel
     * @param z The z coordinate of the pixel
     * @return A packet containing the pixel change
     */
    public PacketContainer updateMapPixel(int x, int z) {
        int mapId = mapView.getId();
        // Create a data array to store the 1 pixel update we'll be performing
        byte[] dataArray = new byte[1];

        // Paint the map to color 24 or gray
        dataArray[0] = (byte) 24;

        // Create and set all the parameters of the packet
        WrapperPlayServerMap mapDataPacket = new WrapperPlayServerMap();
        mapDataPacket.setItemDamage(mapId);
        mapDataPacket.setScale((byte) 4);
        mapDataPacket.setTrackingPosition(false);
        mapDataPacket.setLocked(false);
        // Columns and row are 1 always, don't ask why
        mapDataPacket.setColumns(1);
        mapDataPacket.setRows(1);
        // X and Z here represent the offset, which works for us.
        mapDataPacket.setX(x);
        mapDataPacket.setZ(z);
        mapDataPacket.setData(dataArray);
        // Return the Handle of the wrapped packet.
        return mapDataPacket.getHandle();
    }

}
