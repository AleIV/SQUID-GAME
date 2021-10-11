package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class GlobalListener implements Listener {

    Core instance;

    public GlobalListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onGameTick(GameTickEvent e) {
        Bukkit.getScheduler().runTask(instance, () -> {

        });
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        var games = instance.getGame().getGames();
        if (games.get(GameType.RED_GREEN)) {
            var player = e.getPlayer();
            player.damage(50.0);
        }

    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent e) {
        var player = e.getEntity();

        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);

        //player.getActivePotionEffects().forEach(all -> player.removePotionEffect(all.getType()));
    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent e){
        var entity = e.getEntity();
        var damager = e.getDamager();
        if(entity instanceof Player player){
            var task = new BukkitTCT();

            for (int i = 0; i < 5; i++) {
                task.addWithDelay(new BukkitRunnable(){
                    @Override
                    public void run() {
                        var loc = player.getLocation();
                        if(damager instanceof Projectile){
                            new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true).count(100).offset(0.5, 1, 0.5).extra(0.05).spawn();
                        }else{
                            new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true).count(100).offset(0.1, 0.5, 0.1).extra(0.05).spawn();
                        }
                        
                    }
                }, 50*2);
            }
            
            task.execute();

        }
    }

    //@EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        var player = e.getPlayer();
        if(!player.hasPermission("stands.edit")) return;

        var entity = e.getRightClicked();
        var hand = player.getInventory().getItemInMainHand();

        if(entity instanceof ArmorStand stand && hand != null && hand.getType() ==  Material.STICK){
            var name = hand.getItemMeta().displayName().toString();

            e.setCancelled(true);
            if(name.contains("head")){

                var split = name.split(";");
                var neg = split[1] == "+" ? 1 : -1;
                var pos = split[2];

                var part = stand.getHeadPose();
                var x = part.getX();
                var y = part.getY();
                var z = part.getZ();

                switch (pos) {
                    case "x": stand.setHeadPose(new EulerAngle(x + (0.1*neg), y, z)); break;
                    case "y": stand.setHeadPose(new EulerAngle(x, y + (0.1*neg), z)); break;
                    case "z": stand.setHeadPose(new EulerAngle(x, y, z + (0.1*neg))); break;
                
                    default: break;
                }

            }
            
        }

    }


}
