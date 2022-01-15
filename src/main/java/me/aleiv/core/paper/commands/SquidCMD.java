package me.aleiv.core.paper.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Game.HideMode;
import me.aleiv.core.paper.Game.PvPType;
import me.aleiv.core.paper.Game.TimerType;
import me.aleiv.core.paper.listeners.FrozeListener;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("squid")
@CommandPermission("admin.perm")
public class SquidCMD extends BaseCommand {

    private @NonNull Core instance;
    FrozeListener frozeListener;

    public SquidCMD(Core instance) {
        this.instance = instance;

        this.frozeListener = new FrozeListener(instance);

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("pvp", c -> {
            return Arrays.stream(PvPType.values()).map(m -> m.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("timertype", c -> {
            return Arrays.stream(TimerType.values()).map(m -> m.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("hidemode", c -> {
            return Arrays.stream(HideMode.values()).map(m -> m.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("gamestage", c -> {
            return Arrays.stream(GameStage.values()).map(m -> m.toString()).toList();
        });
    }

    @Subcommand("froze")
    @CommandCompletion("@bool")
    public void froze(CommandSender sender, boolean bool) {
        if(bool){
            instance.registerListener(frozeListener);

        }else{
            instance.unregisterListener(frozeListener);
        }

        sender.sendMessage(ChatColor.DARK_AQUA + "Froze " + bool);
    }

    @Subcommand("sprint")
    @CommandCompletion("@bool")
    public void sprint(CommandSender sender, boolean bool) {
        var game = instance.getGame();
        game.setSprint(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Sprint " + bool);
    }

    @Subcommand("pvp")
    @CommandCompletion("@pvp")
    public void pvp(CommandSender sender, PvPType pvpType) {
        var game = instance.getGame();

        game.setPvp(pvpType);
        sender.sendMessage(ChatColor.DARK_AQUA + "PvP type set to " + pvpType);
    }

    @Subcommand("timerType")
    @CommandCompletion("@timetype")
    public void timerType(CommandSender sender, TimerType timerType) {
        var game = instance.getGame();

        game.setTimerType(timerType);
        sender.sendMessage(ChatColor.DARK_AQUA + "Timer type set to " + timerType);
    }

    @Subcommand("hideMode")
    @CommandCompletion("@hidemode")
    public void hideMode(CommandSender sender, HideMode hideMode) {
        var game = instance.getGame();

        game.setHideMode(hideMode);
        game.refreshHide();
        sender.sendMessage(ChatColor.DARK_AQUA + "Hide mode set to " + hideMode);
    }

    @Subcommand("gameStage")
    @CommandCompletion("@gamestage")
    public void gameStage(CommandSender sender, GameStage gameStage) {
        var game = instance.getGame();

        if(gameStage == GameStage.INGAME){
            game.getGlobalGame().makeAllSleep();
        }

        game.setGameStage(gameStage);
        sender.sendMessage(ChatColor.DARK_AQUA + "Game stage mode set to " + gameStage);
    }

    @Subcommand("timer")
    public void setTimer(CommandSender sender, Integer seconds) {
        var game = instance.getGame();
        var timer = game.getTimer();

        timer.setPreStart(seconds);
        Bukkit.getScheduler().runTaskLater(instance, task -> {
            timer.start(seconds, (int) game.getGameTime());
        }, 20 * 5);
    }

}
