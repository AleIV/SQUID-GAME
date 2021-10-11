package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.events.GameTickEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    HashMap<GameType, Boolean> games = new HashMap<>();

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        games.put(GameType.RED_GREEN, false);
        games.put(GameType.COOKIES, false);

    }

    public enum GameType {
        RED_GREEN, COOKIES
    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }
}