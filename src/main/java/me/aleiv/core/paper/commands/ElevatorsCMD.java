package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("elevators")
@CommandPermission("admin.perm")
public class ElevatorsCMD extends BaseCommand {

    private @NonNull Core instance;

    public ElevatorsCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("doll-elevator")
    public void dollElevator(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getElevators();
        tools.dollElevator(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll elevator " + bool);
    }

    @Subcommand("hideSeek-elevator")
    public void hideSeekElevator(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getElevators();
        tools.hideSeekElevator(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Hide and seek elevator " + bool);
    }

    @Subcommand("elevator1")
    public void elevator1(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getElevators();
        tools.elevator1(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Left elevator " + bool);
    }

    @Subcommand("elevator2")
    public void elevator2(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getElevators();
        tools.elevator2(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Right elevator " + bool);
    }

}
