package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("effects")
@CommandPermission("admin.perm")
public class EffectsCMD extends BaseCommand {

    private @NonNull Core instance;

    public EffectsCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("blood")
    @CommandCompletion("@players")
    public void blood(CommandSender sender, @Flags("other") Player player){
        var effects = instance.getGame().getEffects();
        effects.blood(List.of(player));
    }

    @Subcommand("breathe")
    @CommandCompletion("@players")
    public void breathe(CommandSender sender, @Flags("other") Player player, Integer i, Integer radius){
        var effects = instance.getGame().getEffects();
        var targetLoc = player.getLocation();
        var players = targetLoc.getNearbyPlayers(radius).stream().toList();
        effects.breathe(players, i);
    }

    @Subcommand("stun")
    @CommandCompletion("@players")
    public void stun(CommandSender sender, @Flags("other") Player player, Integer i, Integer radius){
        var effects = instance.getGame().getEffects();
        var targetLoc = player.getLocation();
        var players = targetLoc.getNearbyPlayers(radius).stream().toList();
        effects.stun(players, i);
    }

    @Subcommand("heart")
    @CommandCompletion("@players")
    public void heart(CommandSender sender, @Flags("other") Player player, Integer i, Integer radius){
        var effects = instance.getGame().getEffects();
        var targetLoc = player.getLocation();
        var players = targetLoc.getNearbyPlayers(radius).stream().toList();
        effects.beats(players, i);
    }
    
}
