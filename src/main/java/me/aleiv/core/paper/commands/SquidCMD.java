package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Game.HideMode;
import me.aleiv.core.paper.Game.PvPType;
import me.aleiv.core.paper.Game.TimerType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("squid")
@CommandPermission("admin.perm")
public class SquidCMD extends BaseCommand {

    private @NonNull Core instance;

    public SquidCMD(Core instance) {
        this.instance = instance;

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

    @Subcommand("frozeall")
    @CommandCompletion("@bool")
    public void frozeall(CommandSender sender, boolean bool) {
        this.instance.getGame().setAllFroze(bool);

        sender.sendMessage(ChatColor.DARK_AQUA + "Froze " + bool);
    }

    @Subcommand("froze")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onFroze(Player sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        if (this.instance.getGame().switchFroze(target.getUniqueId())) {
            sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " is now frozen");
        } else {
            sender.sendMessage(ChatColor.RED + "Player " + target.getName() + " is not frozen");
        }
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
