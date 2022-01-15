package me.aleiv.core.paper.Games.chair;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("chair|disco")
@CommandPermission("admin.perm")
public class ChairCMD extends BaseCommand {

    private @NonNull Core instance;

    public ChairCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("notes|music")
    @CommandCompletion("@bool")
    public void door(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getChairGame();
        tools.turnMusic(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "MUSIC TURN " + bool);
    }
}
