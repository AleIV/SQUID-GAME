package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalStage.Stage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandAlias("alias")
@CommandPermission("admin.perm")
public class AliasCMD extends BaseCommand {

    private @NonNull Core instance;

    List<String> listSounds = List.of("dramatic_shot", "fusil_shot", "revolver_shot", "revolver_shot2",
            "impact", "impact2", "heart", "scare", "darkness", "panic", "weird",
            "tension", "breathe", "music_rewind", "daze", "eco_shot", "fart", "cough",
            "full_shots", "trumpet_final");

    public AliasCMD(Core instance) {
        this.instance = instance;

        var manager = instance.getCommandManager();
        manager.getCommandCompletions().registerAsyncCompletion("sfx", c -> {
            return listSounds.stream().toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("stages", c -> {
            return Arrays.stream(Stage.values()).map(m -> m.toString()).toList();
        });


    }

    @Subcommand("stage")
    @CommandAlias("stage")
    @CommandCompletion("@stages")
    public void stage(CommandSender sender, Stage stage) {

        var game = instance.getGame();
        game.setStage(stage);

        sender.sendMessage(ChatColor.DARK_AQUA + "Stage " + stage);
    }

    @Subcommand("sfx")
    @CommandAlias("sfx")
    @CommandCompletion("@sfx")
    public void sfx(Player sender, String str, Integer i) {

        var players = sender.getLocation().getNearbyPlayers(i).stream().toList();
        players.forEach(p -> {
            var loc = p.getLocation();
            p.playSound(loc, "squid:sfx." + str, 1, 1);
        });

        sender.sendMessage(ChatColor.DARK_AQUA + "SFX " + str + " radius " + i);
    }

    @Subcommand("sound")
    @CommandAlias("sound")
    public void speaker(Player sender, String str, Integer i) {

        var players = sender.getLocation().getNearbyPlayers(i).stream().toList();
        players.forEach(p -> {
            var loc = p.getLocation();
            p.playSound(loc, str, 1, 1);
        });

        sender.sendMessage(ChatColor.DARK_AQUA + "Sound " + str + " radius " + i);
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

        var radius = rad == null ? "" : rad + "";
        Bukkit.dispatchCommand(sender, "squidvoice speaker " + bool + radius);

        sender.sendMessage(ChatColor.DARK_AQUA + "Speaker " + bool + radius);
    }

    @Subcommand("saturation")
    @CommandAlias("saturation")
    public void saturation(Player sender, Integer i) {

        var game = instance.getGame();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (game.isPlayer(player)) {
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
