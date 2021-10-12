package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationStore;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("store")
@CommandPermission("admin.perm")
public class AnimationStoreCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public AnimationStoreCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("lights")
    public void game(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Lights " + bool);
        AnimationStore.lights(bool);
    }

    @Subcommand("elevator")
    public void elevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Main elevator " + bool);
        AnimationStore.mainElevator(bool);
    }


}
