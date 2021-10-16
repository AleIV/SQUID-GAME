package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("store")
@CommandPermission("admin.perm")
public class AnimationStoreCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public AnimationStoreCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("lights")
    public void game(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Lights " + bool);

        var tools = instance.getAnimationStore();
        tools.lights(bool);
    }

    @Subcommand("main-elevator")
    public void elevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Main elevator " + bool);
        var tools = instance.getAnimationStore();
        tools.mainElevator(bool);
    }

    @Subcommand("submarine-left")
    public void leftDoor(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Main left door submarine " + bool);
        var tools = instance.getAnimationStore();
        tools.mainLeftDoor(bool);
    }

    @Subcommand("submarine-right")
    public void rightDoor(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Main right door submarine " + bool);
        var tools = instance.getAnimationStore();
        tools.mainRightDoor(bool);
    }

    @Subcommand("make-all-sleep")
    public void makeAllSleep(CommandSender sender){
        sender.sendMessage(ChatColor.DARK_AQUA + "Make all sleep");
        var tools = instance.getAnimationStore();
        tools.makeAllSleep();
    }


}
