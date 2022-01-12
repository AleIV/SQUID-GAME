package me.aleiv.core.paper.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalGame.Clothe;
import me.aleiv.core.paper.Games.GlobalGame.Mask;


@CommandAlias("clothes")
@CommandPermission("admin.perm")
public class ClothesCMD extends BaseCommand {

    private @NonNull Core instance;

    public ClothesCMD(Core instance) {
        this.instance = instance;

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("masks", c -> {
            return Arrays.stream(Mask.values()).map(m -> m.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("uni", c -> {
            return Arrays.stream(Clothe.values()).map(m -> m.toString()).toList();
        });

    }

    @Subcommand("guard")
    @CommandCompletion("@players @masks")
    public void setGuard(CommandSender sender, @Flags("other") Player player, Mask mask){
        var globalGame = instance.getGame().getGlobalGame();
        globalGame.clothes(player, mask);
    }

    @Subcommand("participant")
    @CommandCompletion("@players @uni")
    public void setUni(CommandSender sender, @Flags("other") Player player, Clothe clothe){
        var globalGame = instance.getGame().getGlobalGame();
        globalGame.clothes(player, clothe);
    }

    @Subcommand("all")
    @CommandCompletion("@uni")
    public void setAll(CommandSender sender, Clothe clothe){
        var game = instance.getGame();
        var globalGame = game.getGlobalGame();
        Bukkit.getOnlinePlayers().forEach(player ->{
            if(game.isPlayer(player))
                globalGame.clothes(player, clothe);
            
        });
    }
}
