package me.aleiv.core.paper.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.aleiv.core.paper.Core;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CanceledListener implements Listener {

    Core instance;

    List<Material> bannedMoveList = List.of(Material.NOTE_BLOCK);
    List<Material> bannedSpawnList = List.of(Material.TWISTING_VINES, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, 
    Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS);
    List<Material> bannedDropList = List.of(Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, 
        Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS);
    List<Material> bannedInteract = List.of(Material.ENDER_EYE);
    List<Integer> customModelAntiDrop = List.of(25, 24);

    public CanceledListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        var item = e.getItem();
        var player = e.getPlayer();

        if(item != null && bannedInteract.contains(item.getType())){
            var targetBlock = player.getTargetBlock(5);
            if(targetBlock != null && targetBlock.getType() == Material.END_PORTAL_FRAME) return;
            
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e){
        var cause = e.getCause();
        if(cause == DamageCause.SUFFOCATION){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void checkForMute(AsyncChatEvent e) {
        if (!e.getPlayer().hasPermission("globalmute.talk")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getSpawnReason().toString().contains("NATURAL")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExperience(EntitySpawnEvent e){
        if(e.getEntity() instanceof ExperienceOrb){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e){
        var player = (Player) e.getEntity();
        if(!player.hasPotionEffect(PotionEffectType.SATURATION) && !player.hasPotionEffect(PotionEffectType.HUNGER)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void cancelInteract(PlayerInteractEvent e){
        var player = e.getPlayer();
        var block = e.getClickedBlock();
        var action = e.getAction();

        if(block != null && block.getType() == Material.BIRCH_TRAPDOOR){
            e.setCancelled(true);

            var face = e.getBlockFace();
            if(face == BlockFace.DOWN){
                var loc = block.getRelative(BlockFace.UP).getLocation();
                player.teleport(loc);

            }else if(face == BlockFace.UP){
                var loc = block.getRelative(BlockFace.DOWN).getLocation();
                player.teleport(loc);
            }
            

            return;
        }

        if(block == null || action != Action.RIGHT_CLICK_BLOCK || player.getGameMode() == GameMode.CREATIVE) return;
        
        if(bannedMoveList.contains(block.getType()) || block.getType().toString().contains("TRAPDOOR")
                || block.getType().toString().contains("FENCE_GATE") || (block.getType().toString().contains("JUNGLE_DOOR") && !player.hasPermission("guard.perm"))){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryOpen(InventoryOpenEvent e){
        var player = e.getPlayer();
        var inv = e.getInventory();
        if(player.getGameMode() != GameMode.CREATIVE && !inv.getType().toString().contains("CHEST") && !inv.getType().toString().contains("BARREL") 
            && !inv.getType().toString().contains("LECTERN")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void itemSpawn(ItemSpawnEvent e){
        var item = e.getEntity().getItemStack();
        if(bannedSpawnList.contains(item.getType())){
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

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        var item = e.getItemDrop().getItemStack();
        if(item.getItemMeta().hasCustomModelData() && customModelAntiDrop.contains(item.getItemMeta().getCustomModelData())){
            e.setCancelled(true);
        }
        if(bannedDropList.contains(item.getType())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLectern(PlayerTakeLecternBookEvent e) {
        var player = e.getPlayer();
        var game = instance.getGame();
        if(game.isPlayer(player)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void craft(CraftItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        var player = (Player) e.getWhoClicked();
        if(player.getGameMode() == GameMode.CREATIVE) return;

        var item = e.getCurrentItem();
        var slot = e.getSlotType();

        if (item != null && slot == SlotType.ARMOR){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArrowPick(PlayerPickupArrowEvent e){
        var player = e.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
    }

}



