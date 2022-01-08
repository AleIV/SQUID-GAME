package me.aleiv.core.paper.objects;

import com.github.juliarn.npc.NPC;
import lombok.Getter;
import lombok.Setter;
import me.Fupery.ArtMap.ArtMap;
import me.Fupery.ArtMap.Easel.Canvas;
import me.Fupery.ArtMap.Easel.Easel;
import me.Fupery.ArtMap.Easel.EaselPart;
import me.Fupery.ArtMap.Event.PlayerPaintedEvent;
import me.Fupery.ArtMap.IO.Database.Map;
import me.aleiv.cinematicCore.paper.CinematicTool;
import me.aleiv.cinematicCore.paper.objects.NPCInfo;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.CookieGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CookieCapsule {

    @Getter private Player player;
    private Easel easel;
    @Getter private Location location;
    private Location locCache;
    private NPC npc;
    @Getter private boolean mounted;
    @Getter private boolean blocked;
    @Getter private int errors;
    @Getter private boolean onError;

    private CookieGame.CookieType cookieType;

    private Map artmapMap;
    private ItemStack mapItem;
    private MapView mapView;
    private Canvas canvas;

    private final Material blackBlock = Material.BLACK_CONCRETE;
    private final Material whiteBlock = Material.WHITE_CONCRETE;

    public CookieCapsule(Player player, Location loc, CookieGame.CookieType cookieType) {
        this.player = player;
        this.locCache = this.player.getLocation();
        this.location = loc;
        this.mounted = false;
        this.blocked = false;
        this.errors = 0;
        this.onError = false;

        this.easel = Easel.spawnEasel(loc.clone().set(loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ()+2), BlockFace.NORTH);

        this.cookieType = cookieType;

        this.buildCapsule();
        this.createMap();
    }

    private void buildCapsule() {
        buildFloor(-1, blackBlock);
        buildFloor(3, blackBlock);

        // TODO: Build rest

        buildBlock(0, 1, 2, whiteBlock);
    }

    private void unbuildCapsule() {
        Material air = Material.AIR;
        buildFloor(-1, air);
        buildFloor(3, air);

        // TODO: Unbuild rest

        buildBlock(0, 1, 2, air);
    }

    private void buildFloor(int y, Material mat) {
        this.buildBlock(0, y, 0, mat);
        this.buildBlock(1, y, 0, mat);
        this.buildBlock(1, y, 1, mat);
        this.buildBlock(0, y, 1, mat);
        this.buildBlock(-1, y, 0, mat);
        this.buildBlock(-1, y, -1, mat);
        this.buildBlock(0, y, -1, mat);
        this.buildBlock(-1, y, 1, mat);
        this.buildBlock(1, y, -1, mat);
    }

    private List<Block> getBlocksForCube(Block start, int radius){
        if (radius < 0) {
            return new ArrayList<>();
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

    private void buildBlock(int x, int y, int z, Material mat) {
        location.getWorld().getBlockAt(location.clone().add(x, y, z)).setType(mat);
    }

    private void createMap() {
        ItemStack mapItem = Core.getInstance().getGame().getCookieGame().generateMap(this.cookieType);

        this.mapItem = mapItem;
        this.mapView = ((MapMeta) mapItem.getItemMeta()).getMapView();
        this.artmapMap = new Map(this.mapView.getId());

        this.canvas = new Canvas(this.artmapMap, this.player.getName());
        this.easel.mountCanvas(this.canvas);

        this.artmapMap.update(this.player);
    }

    public void mount() {
        if (this.mounted || this.blocked) return;
        this.mounted = true;

        this.locCache = this.player.getLocation().clone();
        NPCInfo npcInfo = new NPCInfo(this.player);
        NPC npc = CinematicTool.getInstance().getNpcManager().spawnNPC(npcInfo);
        this.npc = npc;

        try {
            ArtMap.instance().getArtistHandler().addPlayer(player, easel,
                    this.artmapMap,
                    EaselPart.getYawOffset(easel.getFacing()));
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 200, false, false, false));
        } catch (ReflectiveOperationException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void unmount(boolean removeEaselUser) {
        if (!this.mounted || this.onError) return;
        this.mounted = false;

        if (removeEaselUser) {
            if (ArtMap.instance().getArtistHandler().containsPlayer(player)) {
                try {
                    ArtMap.instance().getArtistHandler().removePlayer(player);
                } catch (SQLException | IOException e) {
                    ArtMap.instance().getLogger().log(Level.SEVERE, "Database error!", e);
                    return;
                }
            }
        }

        if (this.npc != null) {
            CinematicTool.getInstance().getNpcManager().removeNPC(this.npc);
        }

        this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
        this.player.teleport(this.locCache);
    }

    public void destroy() {
        unbuildCapsule();
        this.easel.breakEasel();
    }

    public void block() {
        this.blocked = true;
        this.unmount(true);
    }

    public void unblock() {
        this.blocked = false;
    }

    public void processEvent(PlayerPaintedEvent e) {
        if (this.onError) {
            e.getPixel().setColour(e.getOldColor());
            return;
        }

        if (e.getOldColor() == -94) {
            this.onError = true;
            this.errors++;
            e.getPixel().setColour((byte) 10);

            // TODO: Pantalla roja, sonidos, etc.
            // Placeholder
            player.sendMessage(ChatColor.RED + "Error ");

            Bukkit.getScheduler().scheduleSyncDelayedTask(CinematicTool.getInstance(), () -> {
                // TODO: Sacar pantalla roja, etc.
                // Placeholder
                player.sendMessage(ChatColor.BLUE + "Continue");
                this.onError = false;
            }, 3*20L);
        } else if (e.getOldColor() == -95 || e.getOldColor() == -96) {
            // Not bad, but not good
            e.getPixel().setColour((byte) 9);
        } else if (e.getOldColor() == -93) {
            // Good
            e.getPixel().setColour((byte) 7);
        } else {
            e.getPixel().setColour(e.getOldColor());
        }
    }


}
