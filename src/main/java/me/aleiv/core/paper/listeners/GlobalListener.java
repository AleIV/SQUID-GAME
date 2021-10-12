package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getSpawnReason().toString().contains("NATURAL")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void cancelInteract(){

    }


}
