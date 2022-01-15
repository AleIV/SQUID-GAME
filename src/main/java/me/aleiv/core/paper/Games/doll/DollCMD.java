package me.aleiv.core.paper.Games.doll;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("doll")
@CommandPermission("admin.perm")
public class DollCMD extends BaseCommand {

    private @NonNull Core instance;

    public DollCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("greenLight")
    @CommandAlias("greenLight")
    public void greenLight(Player sender, @Name("target") @Optional @Flags("other") Player target) {
        var game = instance.getGame();
        game.getDollGame().getGreenLightPanel().open(sender);

    }

    @Subcommand("runLight")
    @CommandAlias("runLight")
    @CommandCompletion("@bool")
    public void runLight(Player sender, Boolean bool) {
        var game = instance.getGame();
        game.getDollGame().runLight(bool);

    }

    
    @Subcommand("shoot")
    @CommandAlias("shoot")
    @CommandCompletion("@players")
    public void shoot(Player sender, @Name("target") @Optional @Flags("other") Player target) {
        AnimationTools.shootLocation(target);
        var effects = instance.getGame().getEffects();
        var targetLoc = target.getLocation();
        var players = targetLoc.getNearbyPlayers(7).stream().toList();
        effects.blood(players);

    }

    @Subcommand("door")
    public void dollDoor(CommandSender sender, Integer i, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll door " + i + " " + bool);
        var tools = instance.getGame().getDollGame();
        switch (i) {
            case 1: tools.dollDoor1(bool); break;
            case 2: tools.dollDoor2(bool); break;
            case 3: tools.dollDoor3(bool); break;
        
            default:
                break;
        }
    }

    @Subcommand("line")
    public void makeAllSleep(CommandSender sender, Integer i, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll line " + i + " " + bool);
        var tools = instance.getGame().getDollGame();
        switch (i) {
            case 1: tools.dollLine1(bool); break;
            case 2: tools.dollLine2(bool); break;
        
            default:
                break;
        }
    }

    @Subcommand("body")
    @CommandCompletion("@bool")
    public void doll(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll " + bool);
        var tools = instance.getGame().getDollGame();
        tools.dollRotate(bool);
    }

    @Subcommand("head")
    @CommandCompletion("@bool")
    public void dollHead(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Doll head" + bool);
        var tools = instance.getGame().getDollGame();
        tools.dollHead(bool);
    }
}
