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

            // TODO: @aleiv hay un fallo
            /*
            [22:26:04 ERROR]: Could not pass event PlayerInteractAtEntityEvent to SquidGame v0.1
java.lang.NullPointerException: Cannot invoke "String.length()" because "name" is null
        at java.util.UUID.fromString(UUID.java:237) ~[?:?]
        at me.aleiv.core.paper.AnimationTools.getEntity(AnimationTools.java:450) ~[?:?]
        at me.aleiv.core.paper.listeners.ItemListener.onMechanics(ItemListener.java:105) ~[?:?]
        at com.destroystokyo.paper.event.executor.asm.generated.GeneratedEventExecutor66.execute(Unknown Source) ~[?:?]
        at org.bukkit.plugin.EventExecutor.lambda$create$1(EventExecutor.java:69) ~[patched_1.16.5.jar:git-Purpur-1171]
        at co.aikar.timings.TimedEventExecutor.execute(TimedEventExecutor.java:80) ~[patched_1.16.5.jar:git-Purpur-1171]
        at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:70) ~[patched_1.16.5.jar:git-Purpur-1171]
        at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:624) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.PlayerConnection.a(PlayerConnection.java:2491) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.PacketPlayInUseEntity.a(PacketPlayInUseEntity.java:55) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.PacketPlayInUseEntity.a(PacketPlayInUseEntity.java:12) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.PlayerConnectionUtils.lambda$ensureMainThread$1(PlayerConnectionUtils.java:55) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.TickTask.run(SourceFile:18) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.IAsyncTaskHandler.executeTask(IAsyncTaskHandler.java:136) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.IAsyncTaskHandlerReentrant.executeTask(SourceFile:23) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.IAsyncTaskHandler.executeNext(IAsyncTaskHandler.java:109) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.MinecraftServer.bb(MinecraftServer.java:1339) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.MinecraftServer.executeNext(MinecraftServer.java:1332) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.IAsyncTaskHandler.awaitTasks(IAsyncTaskHandler.java:119) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.MinecraftServer.sleepForTick(MinecraftServer.java:1308) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.MinecraftServer.w(MinecraftServer.java:1152) ~[patched_1.16.5.jar:git-Purpur-1171]
        at net.minecraft.server.v1_16_R3.MinecraftServer.lambda$a$0(MinecraftServer.java:293) ~[patched_1.16.5.jar:git-Purpur-1171]
        at java.lang.Thread.run(Thread.java:831) [?:?]
            */
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
