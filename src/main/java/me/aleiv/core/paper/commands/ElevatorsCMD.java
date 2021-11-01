package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.Elevators.ElevatorType;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("elevators")
@CommandPermission("admin.perm")
public class ElevatorsCMD extends BaseCommand {

    private @NonNull Core instance;

    public ElevatorsCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("door")
    public void dollElevator(CommandSender sender, ElevatorType elevatorType, Boolean bool){
        var tools = instance.getGame().getElevators();
        tools.elevatorDoor(elevatorType, bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Elevator " + elevatorType + " elevator " + bool);
    }

    @Subcommand("travel")
    public void travel(CommandSender sender, ElevatorType elevator1, ElevatorType elevator2, String music){
        var tools = instance.getGame().getElevators();
        tools.elevatorTravel(elevator1, elevator2, music);
        sender.sendMessage(ChatColor.DARK_AQUA + "Elevator travel " + elevator1 + " to " + elevator2);
    }

}
