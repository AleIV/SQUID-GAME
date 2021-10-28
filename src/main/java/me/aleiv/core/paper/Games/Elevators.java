package me.aleiv.core.paper.Games;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;

public class Elevators {
    Core instance;
    
    public Elevators(Core instance){
        this.instance = instance;

    }

    public void dollElevator(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_ELEVATOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_ELEVATOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_open", 1f, 1f);

            AnimationTools.move("DOLL_ELEVATOR_LEFT", 32, 1, 'z', 0.1f);
            AnimationTools.move("DOLL_ELEVATOR_RIGHT", -32, 1, 'z', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_close", 1f, 1f);

            AnimationTools.move("DOLL_ELEVATOR_LEFT", -32, 1, 'z', 0.1f);
            AnimationTools.move("DOLL_ELEVATOR_RIGHT", 32, 1, 'z', 0.1f);

        }
    }

    public void hideSeekElevator(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("HIDE_SEEK_ELEVATOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("HIDE_SEEK_ELEVATOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_open", 1f, 1f);

            AnimationTools.move("HIDE_SEEK_ELEVATOR_LEFT", -32, 1, 'z', 0.1f);
            AnimationTools.move("HIDE_SEEK_ELEVATOR_RIGHT", 32, 1, 'z', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_close", 1f, 1f);

            AnimationTools.move("HIDE_SEEK_ELEVATOR_LEFT", 32, 1, 'z', 0.1f);
            AnimationTools.move("HIDE_SEEK_ELEVATOR_RIGHT", -32, 1, 'z', 0.1f);

        }
    }

    public void elevator1(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("ELEVATOR1_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("ELEVATOR1_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_open", 1f, 1f);

            AnimationTools.move("ELEVATOR1_LEFT", -32, 1, 'z', 0.1f);
            AnimationTools.move("ELEVATOR1_RIGHT", 32, 1, 'z', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_close", 1f, 1f);

            AnimationTools.move("ELEVATOR1_LEFT", 32, 1, 'z', 0.1f);
            AnimationTools.move("ELEVATOR1_RIGHT", -32, 1, 'z', 0.1f);

        }
    }
    
    public void elevator2(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("ELEVATOR2_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("ELEVATOR2_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_open", 1f, 1f);

            AnimationTools.move("ELEVATOR2_LEFT", 32, 1, 'z', 0.1f);
            AnimationTools.move("ELEVATOR2_RIGHT", -32, 1, 'z', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.tp_elevator_close", 1f, 1f);

            AnimationTools.move("ELEVATOR2_LEFT", -32, 1, 'z', 0.1f);
            AnimationTools.move("ELEVATOR2_RIGHT", 32, 1, 'z', 0.1f);

        }
    }
}
