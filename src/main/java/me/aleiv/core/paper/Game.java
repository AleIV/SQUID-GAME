package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.MainGamePanel;
import me.aleiv.core.paper.objects.RopeGame;
import me.aleiv.core.paper.objects.Timer;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    MainGamePanel mainGamePanel;
    Timer timer;

    RopeGame ropeGame;

    HashMap<String, Role> roles = new HashMap<>();

    Boolean lights = true;
    PvPType pvp = PvPType.ONLY_GUARDS;
    TimerType timerType = TimerType.RED_GREEN;

    String totalPlayers = "000";
    String totalPrize = "000000";

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        this.timer = new Timer(instance, (int) gameTime);

        this.mainGamePanel = new MainGamePanel(instance);

        this.ropeGame = new RopeGame(instance);
    }

    public enum PvPType{
        ONLY_GUARDS, ALL, ONLY_PVP
    }

    public enum Role {
        GUARD, PLAYER
    }

    public enum TimerType{
        RED_GREEN
    }

    public boolean isGuard(Player player){
        return roles.get(player.getUniqueId().toString()) == Role.GUARD;
    }

    public boolean isPlayer(Player player){
        return roles.get(player.getUniqueId().toString()) == Role.PLAYER;
    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }
}