package me.aleiv.core.paper.Games.chicken;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("chicken")
@CommandPermission("admin.perm")
public class ChickenCMD extends BaseCommand {

    ChickenListener listener;
    boolean cover = false;
    private @NonNull Core instance;
    public static ChickenMode chickenMode = ChickenMode.HARD;

    public enum ChickenMode{
        HARD, EASY, MEDIUM, NORMAL
    }

    public ChickenCMD(Core instance){
        this.instance = instance;

        this.listener =  new ChickenListener(instance);

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("chickens", c -> {
            return Arrays.stream(ChickenMode.values()).map(m -> m.toString()).toList();
        });
    }

    @Subcommand("cover")
    @CommandCompletion("@bool")
    public void cover(CommandSender sender, Boolean bool){
        cover = bool;
        if(bool){
            instance.registerListener(listener);
        }else{
            instance.unregisterListener(listener);
        }
        sender.sendMessage(ChatColor.DARK_AQUA + "Cover chicken " + bool);
    }

    @Subcommand("mode")
    @CommandCompletion("@chickens")
    public void mode(CommandSender sender, ChickenMode mode){
        chickenMode = mode;
        sender.sendMessage(ChatColor.DARK_AQUA + "Mode chicken " + mode);
    }

    @Subcommand("reset-button")
    public void resetButton(CommandSender sender) {
        listener.resetButtonPushes();
        sender.sendMessage(ChatColor.DARK_AQUA + "Reset button pushes");
    }

}