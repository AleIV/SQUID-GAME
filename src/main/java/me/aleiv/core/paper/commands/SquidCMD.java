package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import me.aleiv.core.paper.Game.PvPType;
import me.aleiv.core.paper.Game.Role;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("squid|game")
@CommandPermission("admin.perm")
public class SquidCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public SquidCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("game-type")
    public void game(CommandSender sender, GameType gameType, Boolean bool){
        var game = instance.getGame();
        var games = game.getGames();
        if(!games.containsKey(gameType)){
            sender.sendMessage(ChatColor.RED + "Game type doesn't exist.");
            
        }else{

            games.put(gameType, bool);
            sender.sendMessage(ChatColor.DARK_AQUA + "Game type " + gameType + " " + bool);
        }
    }

    @Subcommand("pvp")
    public void pvp(CommandSender sender, PvPType pvpType){
        var game = instance.getGame();

        game.setPvp(pvpType);
        sender.sendMessage(ChatColor.DARK_AQUA + "PvP type set to " + pvpType);
    }

    @Subcommand("role")
    @CommandCompletion("@players")
    public void role(CommandSender sender, @Flags("other") Player player, Role role){
        var game = instance.getGame();
        var roles = game.getRoles();
        var uuid = player.getUniqueId().toString();
        roles.put(uuid, role);
        sender.sendMessage(ChatColor.DARK_AQUA + player.getName() + " role set to " + role.toString().toLowerCase());
    }



}
