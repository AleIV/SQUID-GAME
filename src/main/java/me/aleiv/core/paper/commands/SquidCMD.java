package me.aleiv.core.paper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
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

    }

    @Subcommand("froze")
    public void froze(CommandSender sender, boolean bool) {
        if(bool){
            instance.registerListener(frozeListener);

        }else{
            instance.unregisterListener(frozeListener);
        }

        sender.sendMessage(ChatColor.DARK_AQUA + "Froze " + bool);
    }

    @Subcommand("pvp")
    public void pvp(CommandSender sender, PvPType pvpType) {
        var game = instance.getGame();

        game.setPvp(pvpType);
        sender.sendMessage(ChatColor.DARK_AQUA + "PvP type set to " + pvpType);
    }

    @Subcommand("timerType")
    public void timerType(CommandSender sender, TimerType timerType) {
        var game = instance.getGame();

        game.setTimerType(timerType);
        sender.sendMessage(ChatColor.DARK_AQUA + "Timer type set to " + timerType);
    }

    @Subcommand("hideMode")
    public void hideMode(CommandSender sender, HideMode hideMode) {
        var game = instance.getGame();

        game.setHideMode(hideMode);
        game.refreshHide();
        sender.sendMessage(ChatColor.DARK_AQUA + "Hide mode set to " + hideMode);
    }

    @Subcommand("gameStage")
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
