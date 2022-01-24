package me.aleiv.core.paper.Games.glass;

import com.destroystokyo.paper.ParticleBuilder;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GlassGame {

    private final Core instance;

    private GlassListener glassListener;

    public GlassGame(Core instance){
        this.instance = instance;

        this.glassListener = new GlassListener(instance);
        instance.registerListener(this.glassListener);
    }

    public boolean isGlass(Material material) {
        return material == Material.GLASS;
    }

    public void breakGlass(Block block, Boolean bool) {
        var item = new ItemStack(Material.AIR);
        var type = block.getType();
        if (isGlass(type)) {
            block.breakNaturally(item, true);
            var loc = block.getLocation();
            if(bool){
                AnimationTools.playSoundDistance(loc, 100, "squid:sfx.glass_break", 11f, 1f);
            }
            
            for (BlockFace face : BlockFace.values()) {
                if (face.equals(BlockFace.DOWN) || face.equals(BlockFace.UP) || face.equals(BlockFace.NORTH)
                        || face.equals(BlockFace.EAST) || face.equals(BlockFace.SOUTH) || face.equals(BlockFace.WEST)) {
                    breakGlass(block.getRelative(face), bool);
                }
            }

        }

    }

    public void breakAll(){
        var locations = AnimationTools.findOrderedLocations("GLASSPANE");
        var world = Bukkit.getWorld("world");
        var task = new BukkitTCT();

        
        AnimationTools.playSoundDistance(locations.get(0), 100, "squid:sfx.glass_explosion", 11f, 1f);

        locations.forEach(loc ->{
            task.addWithDelay(new BukkitRunnable(){
                @Override
                public void run(){
                    new ParticleBuilder(Particle.CLOUD).location(loc).receivers(300).force(true).count(100)
                    .offset(1, 1, 1).extra(1).spawn();
                    var block = world.getBlockAt(loc);
                    if(block.getType() == Material.GLASS){
                        breakGlass(block, false);
                    }
                }
            }, 50*5);
        });

        task.execute();
        
    }

    public void addPlayerWithJacket(UUID playerUUID) {
        this.glassListener.addPlayerWithJacket(playerUUID);
    }

    public void removePlayerWithJacket(UUID playerUUID) {
        this.glassListener.removePlayerWithJacket(playerUUID);
    }

    public boolean hasPlayerJacket(UUID playerUUID) {
        return this.glassListener.hasPlayerWithJacket(playerUUID);
    }

    public void addPlayerGlassBreak(UUID playerUUID) {
        this.glassListener.addBreakGlass(playerUUID);
    }

    public void removePlayerGlassBreak(UUID playerUUID) {
        this.glassListener.removeBreakGlass(playerUUID);
    }

    public boolean hasPlayerGlassBreak(UUID playerUUID) {
        return this.glassListener.hasBreakGlass(playerUUID);
    }

    public void forceGlassBreak(Player player) {
        this.glassListener.glassBreakCheck(player.getLocation());
    }

}
