package me.aleiv.core.paper.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import us.jcedeno.libs.rapidinv.ItemBuilder;

public class ItemListener implements Listener {

    Core instance;

    List<Integer> helmetModel = List.of(4, 5, 6, 7, 24, 62, 63, 64, 65);

    public ItemListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onHelmet(PlayerInteractEvent e){
        var item = e.getItem();
        if(item != null && item.getItemMeta().hasCustomModelData() && helmetModel.contains(item.getItemMeta().getCustomModelData())){
            var player = e.getPlayer();
            var equip = player.getEquipment();
            if(equip.getHelmet() == null){
                equip.setHelmet(item);
                equip.setItemInMainHand(null);
            }
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
        var entity = e.getRightClicked();
        var player = e.getPlayer();
        var equip = player.getEquipment();
        var item = equip.getItemInMainHand();
        if(entity instanceof Player target){

            if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if(item.displayName().toString().contains("push")){
                    var direction = player.getLocation().getDirection();
                    target.setVelocity(direction.normalize());
    
                }else if(item.displayName().toString().contains("take")){
    
                    if(player.getPassengers().isEmpty()){
                        player.addPassenger(target);
                        
                    }else{
                        player.getPassengers().forEach(pass ->{
                            pass.eject();
                        });
                    }
                    
                    
                }
            }
        
        }else if(entity instanceof ArmorStand stand){
            var uuid = stand.getUniqueId().toString();
            //TODO:CHECK WELL

            var table = AnimationTools.getEntity("BOX_EYE");
            var inv = player.getInventory();

            if(!inv.contains(Material.FERMENTED_SPIDER_EYE) && !inv.contains(Material.CLAY_BALL)){
                if(table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(1)).build());
                    return;
                }

                table = AnimationTools.getEntity("BOX_RODOLFO");
                if(table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(2)).build());
                    return;
                } 
    
                table = AnimationTools.getEntity("BOX_SQUID");
                if(table.getUniqueId().toString().equals(uuid)){
                    inv.addItem(new ItemBuilder(Material.FERMENTED_SPIDER_EYE).meta(meta -> meta.setCustomModelData(3)).build());
                    return;
                }
    
                table = AnimationTools.getEntity("BOX_CREEPER");
                if(table.getUniqueId().toString().equals(uuid)){
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
