package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    HashMap<String, Role> roles = new HashMap<>();

    Boolean lights = true;
    PvPType pvp = PvPType.ONLY_GUARDS;

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        games.put(GameType.RED_GREEN, false);
        games.put(GameType.COOKIES, false);

    }

    public enum GameType {
        RED_GREEN, COOKIES
    }

    public enum PvPType{
        ONLY_GUARDS, ALL, ONLY_PVP
    }

    public enum Role {
        GUARD, PLAYER
    }

    public boolean isGuard(Player player){
        return roles.get(player.getUniqueId().toString()) == Role.GUARD;
    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }
}