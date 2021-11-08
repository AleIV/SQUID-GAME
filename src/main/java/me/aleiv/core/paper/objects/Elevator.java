package me.aleiv.core.paper.objects;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Data;
import me.aleiv.core.paper.AnimationTools;

@Data
public class Elevator {
    
    Location loc1;
    Location loc2;

    public Elevator(Location loc1, Location loc2){
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public void travel(Elevator elevator, Boolean bool){
        var referencePoint1 = this.getLoc1();
        var referencePoint2 = elevator.getLoc1();

        var offSet = AnimationTools.getOffSet(referencePoint1, referencePoint2);
        var xOffSet = offSet.getX();
        var yOffSet = offSet.getY();
        var zOffSet = offSet.getZ();

        var players = getPlayersInside();
        
        players.forEach(p ->{
            var loc = p.getLocation().clone().add(xOffSet, yOffSet, zOffSet);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 0, false, false, false));
            p.teleport(loc);

            if(bool){
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*1000000, 100, false, false, false));
            }else{
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                p.playSound(loc, "squid:sfx.main_lights_off", 1, 1);
            }

            p.playSound(loc, "squid:sfx.elevator_move", 1, 1);
        });

    }

    public List<Player> getPlayersInside(){
        return AnimationTools.getPlayersInsideCube(loc1, loc2);
    }
}