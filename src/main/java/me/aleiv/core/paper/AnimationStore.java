package me.aleiv.core.paper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AnimationStore {

    Core instance;

    public AnimationStore(Core instance) {
        this.instance = instance;

    }

    public void makeAllSleep(){
        var players = Bukkit.getOnlinePlayers().stream().map(player -> (Player) player).toList();
        var beds = AnimationTools.findLocations("BED");
        AnimationTools.forceSleep(players, beds);
    }
    
    public void lights(Boolean bool){
        instance.getGame().setLights(bool);
        if(bool){
            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*1000000, 100, false, false, false));
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

    public void mainElevator(Boolean bool){
        var loc1 = AnimationTools.parseLocation("MAIN_ELEVATOR_POS1", Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation("MAIN_ELEVATOR_POS2", Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid.elevator_open", 1f, 1f);

            AnimationTools.move("MAIN_RIGHT_ELEVATOR", 33, 1, 'x', 0.1f);
            AnimationTools.move("MAIN_LEFT_ELEVATOR", -33, 1, 'x', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid.elevator_close", 1f, 1f);

            AnimationTools.move("MAIN_RIGHT_ELEVATOR", -33, 1, 'x', 0.1f);
            AnimationTools.move("MAIN_LEFT_ELEVATOR", 33, 1, 'x', 0.1f);

        }
    }

    public void mainLeftDoor(Boolean bool){
        var loc1 = AnimationTools.parseLocation("MAIN_LEFT_DOOR_POS1", Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation("MAIN_LEFT_DOOR_POS2", Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 15, "squid.door_submarine_open", 1f, 1f);

            AnimationTools.rotate("MAIN_LEFT_DOOR", 25, 1);
        

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 15, "squid.door_submarine_close", 1f, 1f);

            AnimationTools.rotate("MAIN_LEFT_DOOR", -25, 1);

        }
    }

    public void mainRightDoor(Boolean bool){
        var loc1 = AnimationTools.parseLocation("MAIN_RIGHT_DOOR_POS1", Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation("MAIN_RIGHT_DOOR_POS2", Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 15, "squid.door_submarine_open", 1f, 1f);

            AnimationTools.rotate("MAIN_RIGHT_DOOR", 25, 1);
        
        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 15, "squid.door_submarine_close", 1f, 1f);

            AnimationTools.rotate("MAIN_RIGHT_DOOR", -25, 1);

        }
    }


}
