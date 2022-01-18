package me.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.Games.Effects;
import me.aleiv.core.paper.Games.Elevators;
import me.aleiv.core.paper.Games.GlobalGame;
import me.aleiv.core.paper.Games.GlobalStage;
import me.aleiv.core.paper.Games.GlobalStage.Stage;
import me.aleiv.core.paper.Games.MainRoom;
import me.aleiv.core.paper.Games.chair.ChairGame;
import me.aleiv.core.paper.Games.cookie.CookieGame;
import me.aleiv.core.paper.Games.doll.DollGame;
import me.aleiv.core.paper.Games.glass.GlassGame;
import me.aleiv.core.paper.Games.hideseek.HideSeekGame;
import me.aleiv.core.paper.Games.potato.PotatoGame;
import me.aleiv.core.paper.Games.rope.RopeGame;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Participant;
import me.aleiv.core.paper.objects.Participant.Role;
import me.aleiv.core.paper.objects.Timer;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    Timer timer;

    boolean allFroze = false;
    List<UUID> frozenPlayers = new ArrayList<>();
    Stage stage = Stage.INGAME;

    //GAMES
    GlobalGame globalGame;
    Effects effects;
    GlobalStage globalStage;

    MainRoom mainRoom;
    DollGame dollGame;
    RopeGame ropeGame;
    HideSeekGame hideSeekGame;
    CookieGame cookieGame;
    Elevators elevators;
    ChairGame chairGame;
    PotatoGame potatoGame;
    GlassGame glassGame;

    HashMap<String, Participant> participants = new HashMap<>();

    Boolean lights = true;
    PvPType pvp = PvPType.ONLY_GUARDS;
    TimerType timerType = TimerType.RED_GREEN;
    HideMode hideMode = HideMode.INGAME;
    GameStage gameStage = GameStage.INGAME;

    String totalPlayers = "000";
    String totalPrize = "000000";

    Location city;
    Location whiteLobby;

    public Game(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        this.timer = new Timer(instance, (int) gameTime);

        this.globalGame = new GlobalGame(instance);
        this.effects = new Effects(instance);
        this.globalStage = new GlobalStage(instance);
        
        this.mainRoom = new MainRoom(instance);
        this.ropeGame = new RopeGame(instance);
        this.dollGame = new DollGame(instance);
        this.hideSeekGame = new HideSeekGame(instance);
        this.cookieGame = new CookieGame(instance);
        this.elevators = new Elevators(instance);
        this.chairGame = new ChairGame(instance);
        this.potatoGame = new PotatoGame(instance);
        this.glassGame = new GlassGame(instance);
    }

    public enum PvPType{
        ONLY_GUARDS, ALL, ONLY_PVP
    }

    public enum TimerType{
        RED_GREEN, GLASS, COOKIE, HIDE_SEEK, POTATO
    }

    public enum HideMode{
        INGAME, LOBBY, TEST
    }

    public enum GameStage{
        INGAME, LOBBY, PAUSE, STARTING
    }

    public enum DeathReason{
        EXPLOSION, PROJECTILE, NORMAL
    }

    public boolean isGuard(Player player){
        return participants.get(player.getUniqueId().toString()).getRole() == Role.GUARD;
    }

    public boolean isPlayer(Player player){
        return participants.get(player.getUniqueId().toString()).getRole() == Role.PLAYER;
    }

    public void refreshHide(){
        var players = Bukkit.getOnlinePlayers().stream().map(p -> (Player) p).toList();

        switch (hideMode) {
            case INGAME -> {
                players.forEach(player ->{
                    var gamemode = player.getGameMode();

                    if(gamemode == GameMode.CREATIVE){
                        players.forEach(p ->{
                            player.showPlayer(instance, p);
                        });
                    }else{
                        players.forEach(p ->{
                            var gm = p.getGameMode();
                            if(gm == GameMode.CREATIVE || gm == GameMode.SPECTATOR){
                                player.hidePlayer(instance, p);
                            }else{
                                player.showPlayer(instance, p);
                            }
                        });
                    }

                });
            }
            case LOBBY ->{
                players.forEach(player ->{
                    var gamemode = player.getGameMode();

                    if(gamemode == GameMode.CREATIVE){
                        players.forEach(p ->{
                            player.showPlayer(instance, p);
                        });
                    }else{
                        players.forEach(p ->{
                            player.hidePlayer(instance, p);
                        });
                    }

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

    public void setFroze(Player player, boolean freeze) {
        if (freeze) {
            this.frozenPlayers.add(player.getUniqueId());
        } else {
            this.frozenPlayers.remove(player.getUniqueId());
        }
    }

    public boolean isPlayerFrozen(UUID playerUUID) {
        return this.frozenPlayers.contains(playerUUID);
    }

    public boolean switchFroze(UUID playerUUID) {
        if (!this.frozenPlayers.remove(playerUUID)) {
            this.frozenPlayers.add(playerUUID);
            return true;
        }
        return false;
    }
}