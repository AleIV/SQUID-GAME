package me.aleiv.core.paper.Games;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.NegativeSpaces;

public class RopeGame {
    Core instance;

    BossBar ropeBar;
    BossBar flagBar;
    BossBar yellowBar;

    @Getter @Setter Integer points = 0;

    String bar = Character.toString('\u0250');
    String rope = Character.toString('\u0251');
    String flag = Character.toString('\u0252');

    public RopeGame(Core instance){
        this.instance = instance;

        this.ropeBar = Bukkit.createBossBar(new NamespacedKey(instance, "ROPE"), "", BarColor.WHITE, BarStyle.SOLID);
        this.flagBar = Bukkit.createBossBar(new NamespacedKey(instance, "FLAG"), "", BarColor.WHITE, BarStyle.SOLID);
        this.yellowBar = Bukkit.createBossBar(new NamespacedKey(instance, "YELLOW"), "", BarColor.WHITE, BarStyle.SOLID);
        ropeBar.setTitle(rope);
        updateBossBar();
        yellowBar.setTitle(bar);
        
        enableRope(true);
    }

    public void updateBossBar(){
        flagBar.setTitle(NegativeSpaces.get(points) + flag);
    }

    public void addBossBars(Player player){
        ropeBar.addPlayer(player);
        flagBar.addPlayer(player);
        yellowBar.addPlayer(player);
    }

    public void enableRope(Boolean bool){
        if(bool){
            ropeBar.setVisible(true);
            flagBar.setVisible(true);
            yellowBar.setVisible(true);

        }else{
            ropeBar.setVisible(false);
            flagBar.setVisible(true);
            yellowBar.setVisible(false);
        }
    }

    public void ropeGate(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("MAIN_RIGHT_DOOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("MAIN_RIGHT_DOOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_open", 1f, 1f);

            AnimationTools.move("ROPE_DOOR1", 42, 1, 'y', 0.1f);
            AnimationTools.move("ROPE_DOOR2", 42, 1, 'y', 0.1f);
            AnimationTools.move("ROPE_DOOR3", 42, 1, 'y', 0.1f);
        
        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_close", 1f, 1f);

            AnimationTools.move("ROPE_DOOR1", -42, 1, 'y', 0.1f);
            AnimationTools.move("ROPE_DOOR2", -42, 1, 'y', 0.1f);
            AnimationTools.move("ROPE_DOOR3", -42, 1, 'y', 0.1f);

        }
    }
    
}
