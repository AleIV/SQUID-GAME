package me.aleiv.core.paper.Games;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class GlassGame {
    Core instance;

    public GlassGame(Core instance){
        this.instance = instance;
    }

    public boolean isGlass(Material material) {
        return material == Material.GLASS;
    }

    public void breakGlass(Block block) {
        var item = new ItemStack(Material.AIR);
        var type = block.getType();
        if (isGlass(type)) {
            block.breakNaturally(item, true);
            for (BlockFace face : BlockFace.values()) {
                if (face.equals(BlockFace.DOWN) || face.equals(BlockFace.UP) || face.equals(BlockFace.NORTH)
                        || face.equals(BlockFace.EAST) || face.equals(BlockFace.SOUTH) || face.equals(BlockFace.WEST)) {
                    breakGlass(block.getRelative(face));
                }
            }

        }

    }

    public void breakAll(){
        var locations = AnimationTools.findOrderedLocations("GLASSPANE");
        var world = Bukkit.getWorld("world");
        var task = new BukkitTCT();

        locations.forEach(loc ->{
            task.addWithDelay(new BukkitRunnable(){
                @Override
                public void run(){
                    new ParticleBuilder(Particle.CLOUD).location(loc).receivers(300).force(true).count(100)
                    .offset(1, 1, 1).extra(1).spawn();
                    var block = world.getBlockAt(loc);
                    if(block.getType() == Material.GLASS){
                        breakGlass(block);
                    }
                    //TODO:GLASS SOUND
                    AnimationTools.playSoundDistance(loc, 50, "sound", 11f, 1f);
                }
            }, 50*5);
        });

        task.execute();
        
    }

}
