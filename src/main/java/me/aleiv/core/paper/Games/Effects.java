package me.aleiv.core.paper.Games;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class Effects {
    Core instance;

    List<String> blood = List.of(Character.toString('\u0261'), Character.toString('\u0262'), Character.toString('\u0263')); 
    Random random = new Random();
    
    public Effects(Core instance) {
        this.instance = instance;
    }

    public void blood(List<Player> players){
        players.forEach(player ->{
            var loc = player.getLocation();
            player.playSound(loc, "squid:sfx.blood", 1, 1);
            instance.showTitle(player, blood.get(random.nextInt(blood.size())), "", 20, 120, 20);
        });
        
    }

    public void stun(List<Player> players, int v){
        var task = new BukkitTCT();

        players.forEach(player ->{
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*v, 5));
        });


        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    players.forEach(player ->{
                        var loc = player.getLocation();
                        player.playSound(loc, "squid:sfx.daze", 1, 1);
                        instance.showTitle(player, Character.toString('\u0260'), "", 20, 60, 20);
                    });
                    
                }
            }, 50 * 20);
        }
        task.execute();
        
    }

    public void beats(List<Player> players, int v){
        var task = new BukkitTCT();

        players.forEach(player ->{
            var loc = player.getLocation();
            player.playSound(loc, "squid:sfx.heart", 1, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*v, 5));
        });

        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    players.forEach(player ->{
                        instance.showTitle(player, Character.toString('\u025F'), "", 0, 10, 0);
                    });
                    
                }
            }, 50 * 20);
        }
        
        var finished = task.execute();
        finished.thenAccept(t ->{
            players.forEach(player ->{
                player.stopSound("squid:sfx.heart");
            });
        });
    }

    public void breathe(List<Player> players, int v){
        var task = new BukkitTCT();

        players.forEach(player ->{
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*v, 5));
        });

        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    players.forEach(player ->{
                        var loc = player.getLocation();
                        player.playSound(loc, "squid:sfx.breathe", 1, 1);
                    });
                    
                }
            }, 50 * 20);
        }
        task.execute();
        
    }

}
