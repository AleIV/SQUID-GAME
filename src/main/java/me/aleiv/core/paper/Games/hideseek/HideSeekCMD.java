package me.aleiv.core.paper.Games.hideseek;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("hideSeek")
@CommandPermission("admin.perm")
public class HideSeekCMD extends BaseCommand {

    private @NonNull Core instance;

    public HideSeekCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("door")
    @CommandCompletion("@bool")
    public void door(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getHideSeekGame();
        tools.door(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Hide Seek door " + bool);
    }

}