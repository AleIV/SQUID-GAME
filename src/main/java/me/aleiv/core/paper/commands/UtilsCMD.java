package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("utils")
@CommandPermission("admin.perm")
public class UtilsCMD extends BaseCommand {

    private @NonNull Core instance;

    public UtilsCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("make-all-sleep")
    public void makeAllSleep(CommandSender sender){
        sender.sendMessage(ChatColor.DARK_AQUA + "Make all sleep");
        var tools = instance.getGame().getMainRoom();
        tools.makeAllSleep();
    }
}