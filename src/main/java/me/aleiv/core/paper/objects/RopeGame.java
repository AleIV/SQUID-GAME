package me.aleiv.core.paper.objects;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.NegativeSpaces;

public class RopeGame {
    Core instance;

    @Getter BossBar bossBar;
    @Getter BossBar yellowBar;

    Integer leftPoints = 0;
    Integer rightPoints = 0;

    public static Integer neg1 = 0;
    public static Integer neg2 = 0;
    public static Integer neg3 = 0;
    public static Integer neg4 = 0;

    String bar = Character.toString('\u0250');
    String rope = Character.toString('\u0251');
    String flag = Character.toString('\u0252');
    String blank = Character.toString('\u0253');

    public RopeGame(Core instance){
        this.instance = instance;

        this.bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "ROPE"), "", BarColor.WHITE, BarStyle.SOLID);
        this.yellowBar = Bukkit.createBossBar(new NamespacedKey(instance, "YELLOW"), "", BarColor.WHITE, BarStyle.SOLID);
        yellowBar.setTitle(bar);
        
        enableRope(true);
        updateBossBar();
    }

    public void updateBossBar(){
        bossBar.setTitle(NegativeSpaces.get(neg1) + rope + NegativeSpaces.get(neg2) + NegativeSpaces.get(neg3) + NegativeSpaces.get(neg4) + flag);
    }

    public void enableRope(Boolean bool){
        if(bool){
            bossBar.setVisible(true);
            yellowBar.setVisible(true);

        }else{
            bossBar.setVisible(false);
            yellowBar.setVisible(false);
        }
    }
    
}
