package me.aleiv.core.paper.Games;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;

public class HideSeekGame {
    Core instance;
    
    public HideSeekGame(Core instance){
        this.instance = instance;

    }

    public void door(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("WOOD_DOOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("WOOD_DOOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.wood_door_open", 1f, 1f);

            AnimationTools.rotate("HIDE_SEEK_DOOR_RIGHT", -12, 1, 0.1f);
            AnimationTools.rotate("HIDE_SEEK_DOOR_LEFT", 12, 1, 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.wood_door_close", 1f, 1f);

            AnimationTools.rotate("HIDE_SEEK_DOOR_RIGHT", 12, 1, 0.1f);
            AnimationTools.rotate("HIDE_SEEK_DOOR_LEFT", -12, 1, 0.1f);

        }
    }

}
