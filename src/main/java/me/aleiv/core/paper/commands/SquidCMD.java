package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("squid")
@CommandPermission("admin.perm")
public class SquidCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public SquidCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("game")
    public void game(CommandSender sender, GameType gameType, Boolean bool){
        var game = instance.getGame();
        var games = game.getGames();
        if(!games.containsKey(gameType)){
            sender.sendMessage(ChatColor.RED + "Game type doesn't exist.");
            
        }else{

            games.put(gameType, bool);
            sender.sendMessage(ChatColor.DARK_AQUA + "Game type " + gameType + " " + bool + ".");
        }
    }


}
