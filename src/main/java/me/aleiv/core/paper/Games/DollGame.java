package me.aleiv.core.paper.Games;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.TimerType;
import me.aleiv.core.paper.objects.GreenLightPanel;

@Data
public class DollGame {
    Core instance;

    @Getter GreenLightPanel greenLightPanel;

    @Setter Location pos1;
    @Setter Location pos2;

    Integer currentSound = 0;

    HashMap<Integer, String> normalLight = new HashMap<Integer, String>() {
        {
            put(5, "squid:sfx.luz_verde_1");
            put(6, "squid:sfx.luz_verde_2");
            put(6, "squid:sfx.luz_verde_6");
            put(5, "squid:sfx.luz_verde_8");

        }
    };
    
    HashMap<Integer, String> fastLight = new HashMap<Integer, String>() {
        {
            put(3, "squid:sfx.luz_verde_3");
            put(4, "squid:sfx.luz_verde_4");
            put(3, "squid:sfx.luz_verde_5");
            put(2, "squid:sfx.luz_verde_7");
        }
    };
    
    public DollGame(Core instance){
        this.instance = instance;

        this.greenLightPanel = new GreenLightPanel(instance);
    }

    public void runLight(Boolean bool){
        var list = bool ? normalLight.entrySet().stream().toList() : fastLight.entrySet().stream().toList(); 
        if(currentSound > 3) currentSound = 0;
        var sound = list.get(currentSound);
        currentSound++;

        var timerLocations = instance.getGame().getTimer().getTimerLocations();
        var loc = timerLocations.get(TimerType.RED_GREEN).get(0);

        AnimationTools.playSoundDistance(loc, 200, sound.getValue(), 11f, 1f);

        greenLightPanel.setGreenLight(true);
        greenLightPanel.updateEnableDisable();
        greenLightPanel.updateMovedPlayers();

        dollHead(true);

        Bukkit.getScheduler().runTaskLater(instance, task ->{
            greenLightPanel.setGreenLight(false);
            greenLightPanel.updateEnableDisable();
            greenLightPanel.updateMovedPlayers();

            dollHead(false);
        }, (20*sound.getKey()) + 20);
    }

    public void dollDoor1(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR1_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("DOLL_DOOR1_POS2"), Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR1_LEFT", 20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR1_RIGHT", -20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_close", 1f, 1f);

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

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR2_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR2_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_close", 1f, 1f);

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

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_open", 1f, 1f);

            AnimationTools.rotate("DOLL_DOOR3_LEFT", -20, 1, 0.1f);
            AnimationTools.rotate("DOLL_DOOR3_RIGHT", 20, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 30, "squid:sfx.doll_door_close", 1f, 1f);

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
        var timerLocations = instance.getGame().getTimer().getTimerLocations();
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
        var timerLocations = instance.getGame().getTimer().getTimerLocations();
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
