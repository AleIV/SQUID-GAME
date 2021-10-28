package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.Games.ChairGame;
import me.aleiv.core.paper.Games.CookieGame;
import me.aleiv.core.paper.Games.DollGame;
import me.aleiv.core.paper.Games.Elevators;
import me.aleiv.core.paper.Games.HideSeekGame;
import me.aleiv.core.paper.Games.MainRoom;
import me.aleiv.core.paper.Games.RopeGame;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Timer;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    Timer timer;

    //GAMES
    MainRoom mainRoom;
    DollGame dollGame;
    RopeGame ropeGame;
    HideSeekGame hideSeekGame;
    CookieGame cookieGame;
    Elevators elevators;
    ChairGame chairGame;

    HashMap<String, Role> roles = new HashMap<>();

    Boolean lights = true;
    PvPType pvp = PvPType.ONLY_GUARDS;
    TimerType timerType = TimerType.RED_GREEN;
    HideMode hideMode = HideMode.INGAME;
    GameStage gameStage = GameStage.INGAME;

    String totalPlayers = "000";
    String totalPrize = "000000";

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        this.timer = new Timer(instance, (int) gameTime);

        this.mainRoom = new MainRoom(instance);
        this.ropeGame = new RopeGame(instance);
        this.dollGame = new DollGame(instance);
        this.hideSeekGame = new HideSeekGame(instance);
        this.cookieGame = new CookieGame(instance);
        this.elevators = new Elevators(instance);
        this.chairGame = new ChairGame(instance);
    }

    public enum PvPType{
        ONLY_GUARDS, ALL, ONLY_PVP
    }

    public enum Role {
        GUARD, PLAYER, DEAD
    }

    public enum TimerType{
        RED_GREEN, GLASS, COOKIE, HIDE_SEEK, POTATO
    }

    public enum HideMode{
        INGAME, LOBBY, TEST
    }

    public enum GameStage{
        INGAME, LOBBY
    }

    public boolean isGuard(Player player){
        return roles.get(player.getUniqueId().toString()) == Role.GUARD;
    }

    public boolean isPlayer(Player player){
        return roles.get(player.getUniqueId().toString()) == Role.PLAYER;
    }

    public void applyInGameHide(Player player){

    }

    public void applyLobbyHide(Player player){

    }

    public void refreshHide(HideMode hideMode){
        this.hideMode = hideMode;

        var players = Bukkit.getOnlinePlayers();

        switch (hideMode) {
            case INGAME ->{
                players.forEach(player ->{
                    applyInGameHide(player);
                });
            }
            case LOBBY ->{
                players.forEach(player ->{
                    applyLobbyHide(player);
                });
            }
            case TEST ->{
                players.forEach(player ->{
                    players.forEach(p ->{
                        player.showPlayer(instance, p);
                   });
                });
            }
        }
    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }
}