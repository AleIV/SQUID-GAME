package me.aleiv.core.paper.objects;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.TimerType;

public class Timer {

    Core instance;

    int startTime;
    int seconds;

    @Getter
    BossBar bossBar;

    String time = "";

    @Setter
    @Getter
    boolean isActive = false;

    int currentClock = 0;

    public Timer(Core instance, int currentTime) {
        this.instance = instance;
        this.seconds = 0;
        this.startTime = (int) currentTime;
        this.bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "TIMER"), "", BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(false);

    }

    private static String timeConvert(int t) {
        int hours = t / 3600;

        int minutes = (t % 3600) / 60;
        int seconds = t % 60;

        return (hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds));
    }

    public int getTime(int currentTime) {
        return (startTime + seconds) - currentTime;
    }

    public void setPreStart(int time) {
        this.time = timeConvert(time);
        this.getBossBar().setVisible(true);
        bossBar.setTitle(this.time);

        var timerLocations = instance.getGame().getTimerLocations();
        if (!timerLocations.containsKey(TimerType.RED_GREEN)) {
            var specialObjects = AnimationTools.specialObjects;
            var loc1 = AnimationTools.parseLocation(specialObjects.get("TIMER_RED_GREEN_1"), Bukkit.getWorld("world"));
            var loc2 = AnimationTools.parseLocation(specialObjects.get("TIMER_RED_GREEN_2"), Bukkit.getWorld("world"));
            var loc3 = AnimationTools.parseLocation(specialObjects.get("TIMER_RED_GREEN_3"), Bukkit.getWorld("world"));
            var loc4 = AnimationTools.parseLocation(specialObjects.get("TIMER_RED_GREEN_4"), Bukkit.getWorld("world"));
            var loc5 = AnimationTools.parseLocation(specialObjects.get("TIMER_RED_GREEN_5"), Bukkit.getWorld("world"));
            List<Location> locations = List.of(loc1, loc2, loc3, loc4, loc5);

            timerLocations.put(TimerType.RED_GREEN, locations);
        }

        instance.getGame().refreshTimer(this.time);
    }

    public void refreshTime(int currentTime) {
        var time = (startTime + seconds) - currentTime;

        if (time < 0) {
            this.time = "00:00";

        } else {
            this.time = timeConvert((int) time);
            bossBar.setTitle(this.time);
            Bukkit.getOnlinePlayers().forEach(player ->{
                player.playSound(player.getLocation(), "squid:sfx.tic", 1, 1);
            });

        }

        if (time < -5) {
            delete();
            setActive(false);
        }

        instance.getGame().refreshTimer(this.time);

    }

    public void delete() {
        bossBar.setVisible(false);

    }

    public void start(int seconds, int startTime) {
        this.time = timeConvert(seconds);
        this.seconds = seconds;
        this.startTime = (int) instance.getGame().getGameTime();
        this.isActive = true;
        bossBar.setVisible(true);
    }

}
