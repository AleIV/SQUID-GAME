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

    @Subcommand("doll-door")
    public void dollDoor(CommandSender sender, Integer i, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll door " + i + " " + bool);
        var tools = instance.getAnimationStore();
        switch (i) {
            case 1: tools.dollDoor1(bool); break;
            case 2: tools.dollDoor2(bool); break;
            case 3: tools.dollDoor3(bool); break;
        
            default:
                break;
        }
    }

    @Subcommand("doll-line")
    public void makeAllSleep(CommandSender sender, Integer i, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll line " + i + " " + bool);
        var tools = instance.getAnimationStore();
        switch (i) {
            case 1: tools.dollLine1(bool); break;
            case 2: tools.dollLine2(bool); break;
        
            default:
                break;
        }
    }

    @Subcommand("doll")
    public void doll(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll " + bool);
        var tools = instance.getAnimationStore();
        tools.dollRotate(bool);
    }

    @Subcommand("doll-head")
    public void dollHead(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll head" + bool);
        var tools = instance.getAnimationStore();
        tools.dollHead(bool);
    }

    @Subcommand("screen-turn")
    public void screen(CommandSender sender, Boolean bool){
        var tools = instance.getAnimationStore();
        tools.turnScreen(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Screen turn " + bool);
    }

    @Subcommand("refresh-prize")
    public void refreshPrize(CommandSender sender, Integer newPrize, Integer delay, Integer value){
        var tools = instance.getAnimationStore();
        tools.refreshPrize(newPrize, delay, value);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed prize " + newPrize + " " + delay);
    }

    @Subcommand("refresh-players")
    public void refreshPlayers(CommandSender sender, Integer newPrize, Integer delay, Integer value){
        var tools = instance.getAnimationStore();
        tools.refreshPlayers(newPrize, delay, value);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed players " + newPrize + " " + delay);
    }




}
