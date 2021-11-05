package me.aleiv.core.paper.map;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.commands.MapCommands;
import me.aleiv.core.paper.map.listener.MapListener;
import me.aleiv.core.paper.map.objects.AsyncCanvas;
import me.aleiv.core.paper.map.packet.WrapperPlayServerMap;

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
    private MapCommands mapCommands;
    public Boolean allowedRotation = false;
    private @Getter Map<MapView, UUID> map = new HashMap<>();
    private @Getter Map<AsyncCanvas, UUID> canvas = new HashMap<>();
    private @Getter ProtocolManager protocolManager;

    /**
     * Constructor.
     * 
     * @param instance The instance of the core.
     */
    public MapSystemManager(Core instance) {
        this.instance = instance;
        this.mapListener = new MapListener(this);
        this.mapCommands = new MapCommands(this);
        this.instance.getCommandManager().registerCommand(this.mapCommands);
        Bukkit.getPluginManager().registerEvents(this.mapListener, this.instance);

        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    /**
     * Gets the map view of a player.
     * 
     * @param map The map view.
     * @return The map view.
     */
    public UUID getOwnerOfMap(MapView map) {
        return this.map.get(map);
    }

    /**
     * Get the owner entry of a canvas.
     * 
     * @param canvas The canvas.
     * @return The Entry containing the {@link AsyncCanvas} as a key and
     *         {@link UUID} as a value.
     */
    public Entry<AsyncCanvas, UUID> getOwnerOfCanvas(MapView canvas) {
        return this.canvas.entrySet().stream().filter(entry -> entry.getKey().getMapView().equals(canvas)).findFirst()
                .orElse(null);
    }

    /**
     * Gets the map views of the UUID.
     * 
     * @param player The player.
     * @return The map views of the player.
     */
    public Set<AsyncCanvas> getMapsOfPlayer(Player player) {
        return this.canvas.entrySet().stream().filter(entry -> entry.getValue().equals(player.getUniqueId()))
                .map(entry -> entry.getKey()).collect(Collectors.toSet());
    }

    /**
     * Update the map of a player with packets
     * 
     * @param mapView The map view to update
     * @param player  The player to update
     */
    public void updateMap(MapView mapView, Player player) {
        int mapId = mapView.getId();
        int dataWidth = 128;
        int dataHeight = 128;
        int dataStartX = 0;
        int dataStartZ = 0;
        int dataLength = dataWidth * dataHeight;
        byte[] dataArray = new byte[dataLength];

        for (int x = 0; x < dataWidth; x++) {
            for (int z = 0; z < dataHeight; z++) {

                dataArray[x + z * dataWidth] = getRandomColorForMap();
            }
        }

        WrapperPlayServerMap mapDataPacket = new WrapperPlayServerMap();
        mapDataPacket.setItemDamage(mapId);
        mapDataPacket.setScale((byte) 4);
        mapDataPacket.setTrackingPosition(false);
        mapDataPacket.setLocked(false);
        mapDataPacket.setColumns(dataWidth);
        mapDataPacket.setRows(dataHeight);
        mapDataPacket.setX(dataStartX);
        mapDataPacket.setZ(dataStartZ);
        mapDataPacket.setData(dataArray);
        player.sendMessage("Sending data for map #" + mapId);

        try {
            this.protocolManager.sendServerPacket(player, mapDataPacket.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void updateMapPixel(MapView mapView, Player player, int x, int z) {
        int mapId = mapView.getId();
        byte[] dataArray = new byte[1];

        // Paint the map to color 24 or gray
        dataArray[0] = (byte) 24;

        WrapperPlayServerMap mapDataPacket = new WrapperPlayServerMap();
        mapDataPacket.setItemDamage(mapId);
        mapDataPacket.setScale((byte) 4);
        mapDataPacket.setTrackingPosition(false);
        mapDataPacket.setLocked(false);
        mapDataPacket.setColumns(1);
        mapDataPacket.setRows(1);
        mapDataPacket.setX(x);
        mapDataPacket.setZ(z);
        mapDataPacket.setData(dataArray);
        player.sendMessage("Sending data for map #" + mapId);

        try {
            this.protocolManager.sendServerPacket(player, mapDataPacket.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private byte getRandomColorForMap() {

        var aray = new byte[12];
        aray[0] = MapPalette.BLUE;
        aray[1] = MapPalette.BROWN;
        aray[2] = MapPalette.DARK_BROWN;
        aray[3] = MapPalette.DARK_GRAY;
        aray[4] = MapPalette.DARK_GREEN;
        aray[5] = MapPalette.GRAY_1;
        aray[6] = MapPalette.GRAY_2;
        aray[7] = MapPalette.LIGHT_BROWN;
        aray[8] = MapPalette.LIGHT_GRAY;
        aray[9] = MapPalette.LIGHT_GREEN;
        aray[10] = MapPalette.PALE_BLUE;
        aray[11] = MapPalette.RED;

        return aray[new Random().nextInt(aray.length)];
    }

}
