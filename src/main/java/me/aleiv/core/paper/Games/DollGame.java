package me.aleiv.core.paper.Games;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import lombok.Getter;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.TimerType;
import me.aleiv.core.paper.objects.GreenLightPanel;

public class DollGame {
    Core instance;

    @Getter GreenLightPanel greenLightPanel;
    
    public DollGame(Core instance){
        this.instance = instance;

        this.greenLightPanel = new GreenLightPanel(instance);
    }

    public void dollDoor1(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR1_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR1_POS2"), Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR1_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR1_RIGHT", -20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_close", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR1_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR1_RIGHT", 20, 1, 0.1f);

        }
    }

    public void dollDoor2(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR2_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR2_POS2"), Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR2_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR2_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_close", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR2_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR2_RIGHT", -20, 1, 0.1f);

        }
    }

    public void dollDoor3(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR3_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR3_POS2"), Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR3_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR3_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 20, "squid:sfx.doll_door_close", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR3_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR3_RIGHT", -20, 1, 0.1f);

        }
    }

    public void dollLine1(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_LINE1_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_LINE1_POS2"), Bukkit.getWorld("world"));
        
        if(bool){
            AnimationTools.fill(loc1, loc2, Material.BARRIER);

        }else{
            AnimationTools.fill(loc1, loc2, Material.AIR);
        }
    }

    public void dollLine2(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_LINE2_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_LINE2_POS2"), Bukkit.getWorld("world"));
        
        if(bool){
            AnimationTools.fill(loc1, loc2, Material.BARRIER);

        }else{
            AnimationTools.fill(loc1, loc2, Material.AIR);
        }
    }

    public void dollRotate(Boolean bool){
        var timerLocations = instance.getGame().getTimerLocations();
        var loc = timerLocations.get(TimerType.RED_GREEN).get(0);
        if(bool){
            AnimationTools.rotate("DOLL_HEAD", 63, 1, 0.05f);
            AnimationTools.rotate("DOLL_BODY", 63, 1, 0.05f);

            AnimationTools.playSoundDistance(loc, 220, "squid:sfx.doll_turn_complete", 1f, 1f);
        }else{
            AnimationTools.rotate("DOLL_BODY", -63, 1, 0.05f);
            AnimationTools.rotate("DOLL_HEAD", -63, 1, 0.05f);
            AnimationTools.playSoundDistance(loc, 220, "squid:sfx.doll_turn_complete2", 1f, 1f);
        }
    }

    public void dollHead(Boolean bool){
        var timerLocations = instance.getGame().getTimerLocations();
        var loc = timerLocations.get(TimerType.RED_GREEN).get(0);
        if(bool){
            AnimationTools.rotate("DOLL_HEAD", 35, 1, 0.09f);
            AnimationTools.playSoundDistance(loc, 220, "squid:sfx.doll_turn", 1f, 1f);
        
        }else{
            AnimationTools.rotate("DOLL_HEAD", -35, 1, 0.09f);
            AnimationTools.playSoundDistance(loc, 220, "squid:sfx.doll_turn2", 1f, 1f);
        }
    }

    
}
