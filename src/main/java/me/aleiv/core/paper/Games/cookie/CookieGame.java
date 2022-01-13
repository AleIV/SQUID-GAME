package me.aleiv.core.paper.Games.cookie;

import lombok.Getter;
import me.Fupery.ArtMap.ArtMap;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.CookieCapsule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CookieGame {
    Core instance;

    private CookieListener cookieListener;
    private boolean started;

    @Getter private final BufferedImage cookieCreeper;
    @Getter private final BufferedImage cookieEye;
    @Getter private final BufferedImage cookieRodolfo;
    @Getter private final BufferedImage cookieSquid;

    @Getter private final List<Location> capsuleLocations;
    private final HashMap<UUID, CookieCapsule> capsules;
    
    public CookieGame(Core instance){
        BufferedImage cookieSquid;
        BufferedImage cookieRodolfo;
        BufferedImage cookieEye;
        BufferedImage cookieCreeper;
        this.instance = instance;
        this.started = false;

        try {
            cookieCreeper = ImageIO.read(Core.getInstance().getResource("cookie_creeper.png"));
            cookieEye = ImageIO.read(Core.getInstance().getResource("cookie_eye.png"));
            cookieRodolfo = ImageIO.read(Core.getInstance().getResource("cookie_rodolfo.png"));
            cookieSquid = ImageIO.read(Core.getInstance().getResource("cookie_squid.png"));
        } catch (IOException e) {
            cookieCreeper = null;
            cookieEye = null;
            cookieRodolfo = null;
            cookieSquid = null;
            e.printStackTrace();
        }

        this.cookieSquid = cookieSquid;
        this.cookieRodolfo = cookieRodolfo;
        this.cookieEye = cookieEye;
        this.cookieCreeper = cookieCreeper;

        this.capsuleLocations = new ArrayList<>();
        this.cookieListener = new CookieListener(instance);
        instance.registerListener(this.cookieListener);

        this.capsules = new HashMap<>();
    }

    public enum CookieType {
        SQUID,
        RODOLFO,
        EYE,
        CREEPER;
    }

    public void cookieDoor1(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var world = Bukkit.getWorld("world");
        var loc1 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR1_POS1"), world);
        var loc2 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR1_POS2"), world);
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_open", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR1_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR1_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_close", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR1_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR1_RIGHT", -20, 1, 0.1f);

        }
    }

    public void cookieDoor2(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var world = Bukkit.getWorld("world");
        var loc1 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR2_POS1"), world);
        var loc2 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR2_POS2"), world);
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_open", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR2_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR2_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_close", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR2_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR2_RIGHT", -20, 1, 0.1f);

        }
    }

    public void cookieDoor3(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var world = Bukkit.getWorld("world");
        var loc1 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR3_POS1"), world);
        var loc2 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR3_POS2"), world);
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_open", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR3_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR3_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_close", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR3_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR3_RIGHT", -20, 1, 0.1f);

        }
    }

    public void cookieDoor4(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var world = Bukkit.getWorld("world");
        var loc1 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR4_POS1"), world);
        var loc2 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR4_POS2"), world);
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_open", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR4_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR4_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_symbol_close", 1f, 1f);

            AnimationTools.rotate("COOKIE_DOOR4_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("COOKIE_DOOR4_RIGHT", -20, 1, 0.1f);

        }
    }

    public void mainDoor(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var world = Bukkit.getWorld("world");
        var loc1 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR_POS1"), world);
        var loc2 = AnimationTools.parseLocation(specialObjects.get("COOKIE_DOOR_POS2"), world);
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_door_open", 1f, 1f);

            AnimationTools.move("COOKIE_DOOR_LEFT", -28, 1, 'z', 0.1f);
            AnimationTools.move("COOKIE_DOOR_RIGHT", 28, 1, 'z', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 50, "squid:sfx.cookie_door_close", 1f, 1f);

            AnimationTools.move("COOKIE_DOOR_LEFT", 28, 1, 'z', 0.1f);
            AnimationTools.move("COOKIE_DOOR_RIGHT", -28, 1, 'z', 0.1f);

        }
    }

    public void start(List<Location> locs) {
        this.started = true;
        this.capsuleLocations.addAll(locs);
    }

    public void stop() {
        this.started = false;
        this.capsuleLocations.clear();

        this.capsules.forEach((uuid, c) -> {
            c.unmount(true);
            c.destroy();
        });
        this.capsules.clear();
    }

    public ItemStack generateMap(CookieType type) {
        BufferedImage image;
        switch (type) {
            case CREEPER -> image = this.cookieCreeper;
            case EYE -> image = this.cookieEye;
            case SQUID -> image = this.cookieSquid;
            case RODOLFO -> image = this.cookieRodolfo;
            default -> image = null;
        }

        if (image == null) {
            return null;
        }

        MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));
        map.setScale(MapView.Scale.FARTHEST);
        map.getRenderers().clear();
        byte[] colors = new byte[image.getWidth() * image.getHeight()];
        map.addRenderer(new MapRenderer() {
            @Override
            public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
                canvas.drawImage(0, 0, image);

                // Loop through colors, and set them to array colors
                int x = 0;
                int y = 0;
                while (y < 128) {
                    colors[x + (y * 128)] = canvas.getPixel(x, y);
                    x++;
                    if (x == 128) {
                        x = 0;
                        y++;
                    }
                }
            }
        });
        try {
            ArtMap.instance().getReflection().setWorldMap(map, colors);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) item.getItemMeta();
        meta.setMapView(map);
        item.setItemMeta(meta);

        return item;
    }

    public CookieCapsule createCapsule(Player player, CookieType type) {
        Location loc = null;
        if (this.capsules.containsKey(player.getUniqueId())) {
            CookieCapsule capsule = this.capsules.get(player.getUniqueId());
            capsule.unmount(true);
            capsule.destroy();
            loc = capsule.getLocation();
            this.capsules.remove(player.getUniqueId());
        }

        if (loc == null) {
            loc = this.capsuleLocations.remove(0);
        }
        if (loc == null) {
            player.sendMessage(ChatColor.RED + "No hay mas capsulas disponibles!");
            return null;
        }

        CookieCapsule cookieCapsule = new CookieCapsule(player, loc, type);
        this.capsules.put(player.getUniqueId(), cookieCapsule);

        return cookieCapsule;
    }

    public CookieCapsule getCapsule(Player player) {
        if (!this.started) return null;

        return this.capsules.get(player.getUniqueId());
    }

    public void destroyCapsule(Player player) {
        CookieCapsule cc = this.capsules.remove(player.getUniqueId());
        cc.destroy();
    }

    public List<CookieCapsule> getAllCapules() {
        return new ArrayList<>(this.capsules.values());
    }

    public boolean isStarted() {
        return started;
    }
}
