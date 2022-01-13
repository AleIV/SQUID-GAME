package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.cinematicCore.paper.CinematicTool;
import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("cinema")
@CommandPermission("admin.perm")
public class CinemaCMD extends BaseCommand {

    private @NonNull Core instance;

    public CinemaCMD(Core instance) {
        this.instance = instance;

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("cinema", c -> {
            return Arrays.stream(Cinema.values()).map(m -> m.toString()).toList();
        });

    }

    public enum Cinema{
        PIG, //ALL
        INTRO, DISCURSO, DOLL, HIDESEEK, //DAY 1
        COOKIE, //DAY 2
        ROPE, //DAY 3
        CHAIR, CHICKEN, //DAY 4
        PHONE, GLASS, //DAY 5
        SQUIDGAME //DAY 6

    }

    @Subcommand("squidgame")
    @CommandCompletion("@players")
    public void squidgame(Player sender, @Flags("other") Player player1, @Flags("other") Player player2){
        //TODO: FINAL CINEMA
    }

    @Subcommand("play")
    @CommandCompletion("@cinema")
    public void play(Player sender, Cinema cinema, @Optional Integer radius){
        var cine = CinematicTool.getInstance().getGame();   
        var players = Bukkit.getOnlinePlayers().stream().map(p -> (Player)p).toList();
        if(radius != null){
            players = sender.getLocation().getNearbyPlayers(radius).stream().toList();
        }
        var uuids = players.stream().map(p->p.getUniqueId()).toList();
        
        switch (cinema) {
            case PIG -> {
                cine.play(uuids, "PIG");
            }
            case CHAIR -> {
                cine.play(uuids, "CHAIR");
            }
            case CHICKEN -> {
                cine.play(uuids, "CHICKEN");
            }
            case COOKIE -> {
                cine.play(uuids, "COOKIE");
            }
            case DISCURSO -> {
                cine.play(uuids, "DISCURSO");
            }
            case DOLL -> {
                cine.play(uuids, "DOLL");
            }
            case GLASS -> {
                cine.play(uuids, "GLASS");
            }
            case HIDESEEK -> {
                cine.play(uuids, "HIDE");
            }
            case INTRO -> {
                cine.play(uuids, "PART1", "BOAT1", "ISLAND");
            }
            case PHONE -> {
                cine.play(uuids, "PHONE");
            }
            case ROPE -> {
                cine.play(uuids, "ROPE");
            }
            case SQUIDGAME -> {
                cine.play(uuids, "SQUIDGAME");
            }

            default -> throw new IllegalArgumentException("Unexpected value: " + cinema);
        }
        
    }
    
}
