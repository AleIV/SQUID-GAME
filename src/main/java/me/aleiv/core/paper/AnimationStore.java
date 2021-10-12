package me.aleiv.core.paper;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AnimationStore {

    static Core instance;

    public AnimationStore(Core instance) {
        AnimationStore.instance = instance;

    }

    public static void lights(Boolean bool){
        if(bool){
            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 100, false, false, false));
                player.playSound(loc, "squid.lights_on", 1, 1);
            });

        }else{
            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(loc, "squid.lights_off", 1, 1);
            });

        }
    }

    public static void mainElevator(Boolean bool){
        if(bool){
            AnimationTools.move("RIGHT_ELEVATOR", 33, 1, 'x', 0.1f);
            AnimationTools.move("LEFT_ELEVATOR", -33, 1, 'x', 0.1f);

            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.playSound(loc, "squid.elevator_open", 1, 1);
            });

        }else{
            
            AnimationTools.move("RIGHT_ELEVATOR", -33, 1, 'x', 0.1f);
            AnimationTools.move("LEFT_ELEVATOR", 33, 1, 'x', 0.1f);

            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.playSound(loc, "squid.elevator_close", 1, 1);
            });
        }
    }


}
