package me.aleiv.core.paper.listeners;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;

import me.aleiv.core.paper.Core;

public class CanceledListener implements Listener {

    Core instance;

    List<Material> bannedMoveList = List.of(Material.NOTE_BLOCK);

    public CanceledListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getSpawnReason().toString().contains("NATURAL")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void cancelInteract(PlayerInteractEvent e){
        var player = e.getPlayer();
        var block = e.getClickedBlock();
        var action = e.getAction();

        if(block == null || action != Action.RIGHT_CLICK_BLOCK || player.getGameMode() == GameMode.CREATIVE) return;

        if(block.getType() == Material.DARK_OAK_TRAPDOOR) return;
        
        if(bannedMoveList.contains(block.getType()) || block.getType().toString().contains("TRAPDOOR") 
                || block.getType().toString().contains("FENCE_GATE")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryOpen(InventoryOpenEvent e){
        var player = e.getPlayer();
        var inv = e.getInventory();
        if(player.getGameMode() != GameMode.CREATIVE && !inv.getType().toString().contains("CHEST")){
            e.setCancelled(true);
        }
    }

    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){
        var world = e.getWorld();
        var game = instance.getGame();
        var doll = game.getDollGame();
        doll.setPos1(new Location(world, 470, 65, 22));
        doll.setPos2(new Location(world, 326, 80, 106));
    }
}



