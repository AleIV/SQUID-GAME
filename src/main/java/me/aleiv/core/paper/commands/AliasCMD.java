package me.aleiv.core.paper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("alias")
@CommandPermission("admin.perm")
public class AliasCMD extends BaseCommand {

    private @NonNull Core instance;

    public AliasCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("g|globalmute")
    @CommandAlias("g|globalmute")
    @CommandCompletion("@bool")
    public void game(CommandSender sender, Boolean bool) {

        Bukkit.dispatchCommand(sender, "squidvoice globalmute " + bool);

        sender.sendMessage(ChatColor.DARK_AQUA + "Globalmute " + bool);
    }

    @Subcommand("dis|distance")
    @CommandAlias("dis|distance")
    public void distance(CommandSender sender, Integer distance) {

        Bukkit.dispatchCommand(sender, "squidvoice distance " + distance);

        sender.sendMessage(ChatColor.DARK_AQUA + "Distance Voice " + distance);
    }
    
    @Subcommand("speaker|s")
    @CommandAlias("speaker|s")
    @CommandCompletion("@bool")
    public void speaker(CommandSender sender, Boolean bool, @Optional Integer rad) {

        var radius =  rad == null ? "" : rad + "";
        Bukkit.dispatchCommand(sender, "squidvoice speaker " + bool + radius);

        sender.sendMessage(ChatColor.DARK_AQUA + "Speaker " + bool + radius);
    }

    @Subcommand("sound")
    @CommandAlias("sound")
    public void speaker(Player sender, String str, Integer i) {

        var players =  sender.getLocation().getNearbyPlayers(i).stream().toList();
        players.forEach(p ->{
            var loc = p.getLocation();
            p.playSound(loc, str, 1, 1);
        });

        sender.sendMessage(ChatColor.DARK_AQUA + "Sound " + str + " radius " + i);
    }

    @Subcommand("saturation")
    @CommandAlias("saturation")
    public void saturation(Player sender, Integer i) {

        var game = instance.getGame();
        Bukkit.getOnlinePlayers().forEach(player ->{
            if(game.isPlayer(player)){
                player.setFoodLevel(i);
            }
        });

        sender.sendMessage(ChatColor.DARK_AQUA + "Sound " + i);
    }

    @CommandCompletion("/command1 & /commandn+1 ")
    @CommandAlias("cc|ccmd")
    @Subcommand("cc|ccmd")
    public void rotate(Player sender, String args) {
        // Split the commands to be executed by && and execute them
        String[] cmds = args.split("&");
        for (String cmd : cmds) {
            var cleanedUpCommand = cmd.trim().substring(1);
            sender.sendMessage(Core.getMiniMessage()
                    .parse(String.format("<green>Executing command <white>%s ", cleanedUpCommand)));
            Bukkit.dispatchCommand(sender, cleanedUpCommand);

        }

    }

}
