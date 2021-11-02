package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("rope")
@CommandPermission("admin.perm")
public class RopeCMD extends BaseCommand {

    private @NonNull Core instance;

    public RopeCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("guillotine")
    public void guillotine(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getRopeGame();
        tools.moveGuillotine(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Guillotine move " + bool);
    }

    @Subcommand("gate")
    public void gate(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Rope gate " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.ropeGate(bool);
    }

    @Subcommand("bossbar")
    public void bossbar(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Bossbar rope " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.enableRope(bool);
    }

    @Subcommand("right-elevator")
    public void rightElevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Right elevator " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.rightElevator(bool);
    }

    @Subcommand("left-elevator")
    public void leftElevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Left elevator " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.leftElevator(bool);
    }

}
