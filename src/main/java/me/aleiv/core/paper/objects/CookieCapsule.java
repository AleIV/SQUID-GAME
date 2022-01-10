package me.aleiv.core.paper.objects;

import com.github.juliarn.npc.NPC;
import lombok.Getter;
import me.Fupery.ArtMap.ArtMap;
import me.Fupery.ArtMap.Easel.Canvas;
import me.Fupery.ArtMap.Easel.Easel;
import me.Fupery.ArtMap.Easel.EaselPart;
import me.Fupery.ArtMap.Event.PlayerPaintedEvent;
import me.Fupery.ArtMap.IO.Database.Map;
import me.Fupery.ArtMap.Painting.CanvasRenderer;
import me.aleiv.cinematicCore.paper.CinematicTool;
import me.aleiv.cinematicCore.paper.objects.NPCInfo;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.CookieGame;
import me.aleiv.modeltool.utilities.RandomUtils;
import org.bukkit.*;
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
    @Getter private boolean done;
    @Getter private int errors;
    @Getter private boolean onError;

    private CookieGame.CookieType cookieType;

    private Map artmapMap;
    private ItemStack mapItem;
    private MapView mapView;
    private Canvas canvas;

    private final Material BLACK_BLOCK = Material.BLACK_CONCRETE;
    private final Material WHITE_BLOCK = Material.GLOWSTONE;
    private final byte RED_COLOR = 18;
    private final byte OUTSIDE_COLOR = 9;
    private final byte GREEN_COLOR = 7;

    public CookieCapsule(Player player, Location loc, CookieGame.CookieType cookieType) {
        this.player = player;
        this.locCache = this.player.getLocation().clone();
        this.location = loc;
        this.mounted = false;
        this.blocked = false;
        this.done = false;
        this.errors = 0;
        this.onError = false;

        this.easel = Easel.spawnEasel(loc.clone().set(loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ()+2), BlockFace.NORTH);

        this.cookieType = cookieType;

        this.buildCapsule();
        this.createMap();
    }

    private void buildCapsule() {
        List<Block> outerBlocks = this.getBlocksForCube(location.clone().add(0, 1, 0).getBlock(), 4);
        List<Block> innerBlocks = this.getBlocksForCube(location.clone().add(0, 1, 0).getBlock(), 3);
        outerBlocks.forEach(b -> b.setType(BLACK_BLOCK));
        innerBlocks.forEach(b -> b.setType(Material.AIR));
        buildBlock(0, 1, 2, WHITE_BLOCK);
    }

    private void unbuildCapsule() {
        List<Block> outerBlocks = this.getBlocksForCube(location.clone().add(0, 1, 0).getBlock(), 4);
        outerBlocks.forEach(b -> b.setType(Material.AIR));
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

        player.playSound(player.getLocation(), "squid:sfx.cookie_box_open", 1, 1);
        // TODO: Aplicar blindness a lo mejor?

        this.locCache = this.player.getLocation().clone();
        NPCInfo npcInfo = new NPCInfo(this.player);
        NPC npc = CinematicTool.getInstance().getNpcManager().spawnNPC(npcInfo);
        this.npc = npc;

        try {
            ArtMap.instance().getArtistHandler().addPlayer(player, easel,
                    this.artmapMap,
                    EaselPart.getYawOffset(easel.getFacing()));
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 200, false, false, false));
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false, false));
        } catch (ReflectiveOperationException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void unmount(boolean removeEaselUser) {
        if (!this.mounted) return;
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
        this.player.removePotionEffect(PotionEffectType.SLOW);
        this.player.teleport(this.locCache);
        Bukkit.getScheduler().scheduleSyncDelayedTask(ArtMap.instance(), () -> this.player.teleport(this.locCache), 2L);
        this.player.playSound(player.getLocation(), "squid:sfx.cookie_box_close", 1, 1);
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

        if (e.getOldColor() == -94 || e.getOldColor() == -96) {
            this.onError = true;
            this.errors++;
            e.getPixel().setColour(RED_COLOR);

            player.playSound(player.getLocation(), "squid:sfx.cookie_break_loud", 1f, 1f);
            player.sendTitle("\u025D", "", 5, 5, 50);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4, false, false, false));

            Bukkit.getScheduler().scheduleSyncDelayedTask(CinematicTool.getInstance(), () -> {
                this.onError = false;
                if (this.mounted) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false, false));
                }
            }, 3*20L);
        } else if (e.getOldColor() == -95) {
            // Not bad, but not good
            e.getPixel().setColour(OUTSIDE_COLOR);
        } else if (e.getOldColor() == -93) {
            // Good
            if (RandomUtils.generateInt(0, 2) == 2) {
                player.playSound(player.getLocation(), "squid:sfx.cookie_break", 1f, (float) (RandomUtils.generateInt(80, 150)/100));
            }

            // TODO: Particles
            //player.spawnParticle(Particle.FALLING_DUST, player.getLocation().getDirection().);

            e.getPixel().setColour(GREEN_COLOR);
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
                CanvasRenderer renderer = e.getCanvasRenderer();

                // Check if there is any byte left with a value of -93
                boolean hasGood = false;
                for (int i = 0; i < renderer.getAxisLength(); i++) {
                    for (int j = 0; j < renderer.getAxisLength(); j++) {
                        if (renderer.getPixel(i, j) == -93) {
                            hasGood = true;
                        }
                    }
                }

                if (!hasGood) {
                    Bukkit.getScheduler().runTask(Core.getInstance(), this::win);
                }
            });
        } else {
            e.getPixel().setColour(e.getOldColor());
        }
    }

    public void win() {
        this.done = true;
        this.onError = false;
        this.block();

        Bukkit.getOnlinePlayers().parallelStream().filter(p -> Core.getInstance().getGame().isGuard(p)).forEach(player -> {
            player.sendMessage(ChatColor.GREEN + this.player.getName() + " ha terminado la galleta.");
        });

        player.playSound(player.getLocation(), "squid:sfx.right", 2f, 1f);
        this.player.sendTitle(ChatColor.WHITE + " ", ChatColor.GREEN + "Has completado la galleta", 4, 20, 40);
    }


}
