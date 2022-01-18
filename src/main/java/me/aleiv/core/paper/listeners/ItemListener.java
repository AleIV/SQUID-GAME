package me.aleiv.core.paper.listeners;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalGame.Clothe;
import us.jcedeno.libs.rapidinv.ItemBuilder;

public class ItemListener implements Listener {

    Core instance;

    List<Integer> helmetModel = List.of();

    public ItemListener(Core instance){
        this.instance = instance;
    }


    @EventHandler
    public void onHelmet(PlayerInteractEvent e){
        var item = e.getItem();
        if(item != null && item.getItemMeta().hasCustomModelData() && helmetModel.contains(item.getItemMeta().getCustomModelData()) && item.getType() != Material.FERMENTED_SPIDER_EYE){
            var player = e.getPlayer();
            var equip = player.getEquipment();
            if(equip.getHelmet() == null){
                equip.setHelmet(item);
                equip.setItemInMainHand(null);
            }
        }

        var block =  e.getClickedBlock();
        if(block != null && block.getType() == Material.PURPLE_CARPET){
            block.setType(Material.AIR);
            var player = e.getPlayer();
            var clothes = instance.getGame().getGlobalGame();
            clothes.clothes(player, Clothe.SMOKIN);
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        var entity = e.getEntity();
        var proj = e.getProjectile();
        if(entity instanceof Player player && proj instanceof Arrow arrow){
            var vector = player.getLocation().getDirection();
            vector.add(vector);
            vector.add(vector);
            arrow.setVelocity(vector);
        }
        
    }

    @EventHandler
    public void shoot(EntityShootBowEvent e){
        var item = e.getBow();
        var entity = e.getEntity();
        if(entity instanceof Player player && item != null && item.getItemMeta().hasCustomModelData() 
            && item.getItemMeta().getCustomModelData() == 1){
                var loc = player.getLocation();
                AnimationTools.playSoundDistance(loc, 40, "squid:sfx.revolver_shot", 1f, 1f);
        }
    }

    @EventHandler
    public void onMechanics(PlayerInteractAtEntityEvent e){
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        ItemStack item = null;
        if (e.getHand() == EquipmentSlot.HAND) {
            item = e.getPlayer().getInventory().getItemInMainHand();
        } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
            item = e.getPlayer().getInventory().getItemInOffHand();
        }


        if (entity instanceof Player target && item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String name = item.getItemMeta().displayName().toString();
            if (name.contains("push")) {
                var direction = player.getLocation().getDirection();
                target.setVelocity(direction.normalize());
            } else if (name.contains("take")) {
                if (player.getPassengers().isEmpty()) {
                    player.addPassenger(target);
                } else {
                    player.getPassengers().forEach(Entity::eject);
                }
            } else if (name.contains("froze")) {
                this.instance.getGame().switchFroze(target.getUniqueId());
            } else if (name.contains("info")) {
                player.sendMessage("Player Name: §e" + target.getName());
                player.sendMessage("Player UUID: §e" + target.getUniqueId());
            } else if (name.contains("shot")) {
                AnimationTools.shootLocation(target);
                var effects = instance.getGame().getEffects();
                var targetLoc = target.getLocation();
                var players = targetLoc.getNearbyPlayers(7).stream().toList();
                effects.blood(players);
            }
        } else if (entity instanceof ArmorStand stand) {
            var uuid = stand.getUniqueId().toString();
            //TODO:CHECK WELL

            var table = AnimationTools.getEntity("BOX_EYE");
            var inv = player.getInventory();

            if(table != null && !inv.contains(Material.FERMENTED_SPIDER_EYE) && !inv.contains(Material.CLAY_BALL)){
                if(table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(1)).build());
                    return;
                }

                table = AnimationTools.getEntity("BOX_RODOLFO");
                if(table != null && table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(2)).build());
                    return;
                } 
    
                table = AnimationTools.getEntity("BOX_SQUID");
                if(table != null && table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(3)).build());
                    return;
                }
    
                table = AnimationTools.getEntity("BOX_CREEPER");
                if(table != null && table.getUniqueId().toString().equals(uuid)){
                    if(table.getUniqueId().toString().equals(uuid)){
                        inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(0)).build());
                        return;
                    }
                    return;
                }
    
            }
            
        }
    }

    @EventHandler
    public void onTakeFood(PlayerInteractEvent e){
        var player = e.getPlayer();
        var block = player.getTargetBlock(5);
        if(block != null && block.getType() == Material.END_PORTAL_FRAME){
            var inv = player.getInventory();
            if(!inv.contains(Material.MUSHROOM_STEW) && !inv.contains(Material.POTION) && 
                !inv.contains(Material.GLASS_BOTTLE) && !inv.contains(Material.BOWL)){

                inv.addItem(new ItemStack(Material.MUSHROOM_STEW));
                var bottle = new ItemBuilder(Material.POTION).flags(ItemFlag.HIDE_POTION_EFFECTS).name("Soda").build();
                inv.addItem(bottle);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        var item = e.getItem();
        if(item.getType() == Material.MUSHROOM_STEW){
            var player = e.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20*10, 0));
        }
    }

    @EventHandler
    public void onBreakGlass(ProjectileHitEvent e){
        var proj = e.getEntity();
        if(proj instanceof Snowball snowball){
            var loc = proj.getLocation();
            AnimationTools.playSoundDistance(loc, 20, "squid:sfx.bottle_break", 1f, 1f);
            var bottle = new ItemBuilder(Material.GOLDEN_SWORD).meta(meta -> meta.setCustomModelData(1))
                .flags(ItemFlag.HIDE_ATTRIBUTES).name("Bottle").build();
            loc.getWorld().dropItemNaturally(loc, bottle);

        }
    }


}
