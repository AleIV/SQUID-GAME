package me.aleiv.core.paper.commands;

import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("utils")
@CommandPermission("admin.perm")
public class UtilsCMD extends BaseCommand {

    private @NonNull Core instance;

    public UtilsCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("make-all-sleep")
    public void makeAllSleep(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Make all sleep");
        var game = instance.getGame();
        var tools = game.getGlobalGame();
        
        tools.makeAllSleep();
    }

    @Subcommand("sleep")
    public void shoot(Player sender, @Name("target") @Optional @Flags("other") Player target) {
        var loc = sender.getTargetBlock(5);
        if (loc != null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Sleep " + target.getName() + " sleep");
            var tools = instance.getGame().getGlobalGame();
            tools.sleep(target, loc.getLocation());
        }

    }

    @Subcommand("set-spawn")
    public void setSpawn(Player sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Set Spawn");
        var tools = instance.getGame();
        tools.setCity(sender.getLocation());
    }

    @Subcommand("sleep-instant")
    public void sleepInstant(Player sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + "sleep-instant");

        var game = instance.getGame();
        var beds = AnimationTools.findLocations("BED");
        var guardBeds = AnimationTools.findLocations("GUARDB");
        var players = Bukkit.getOnlinePlayers();
        var participants = players.stream().filter(player -> game.isPlayer(player)).map(player -> (Player) player).toList();
        var guards = players.stream().filter(player -> game.isGuard(player)).map(player -> (Player) player).toList();
        
        AnimationTools.forceSleepInstant(guards, guardBeds);
        AnimationTools.forceSleepInstant(participants, beds);
    }

    @Subcommand("sleep-guards")
    public void guards(Player sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + "guards-instant");

        var game = instance.getGame();
        var guardBeds = AnimationTools.findLocations("GUARDB");
        var players = Bukkit.getOnlinePlayers();
        var guards = players.stream().filter(player -> game.isGuard(player)).map(player -> (Player) player).toList();
        
        AnimationTools.forceSleepInstant(guards, guardBeds);
    }

    @Subcommand("sleep-players")
    public void sleepPlauers(Player sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + "players-instant");

        var game = instance.getGame();
        var beds = AnimationTools.findLocations("BED");
        var players = Bukkit.getOnlinePlayers();
        var participants = players.stream().filter(player -> game.isPlayer(player)).map(player -> (Player) player).toList();
        
        AnimationTools.forceSleepInstant(participants, beds);
    }

    @Subcommand("gas")
    public void sendGas(CommandSender sender) {
        var tools = instance.getGame();
        tools.getGlobalGame().applyGas(Bukkit.getOnlinePlayers().stream().map(p -> (Player) p).toList());
    }

    @Subcommand("countdown")
    public void sendCount(CommandSender sender) {
        var tools = instance.getGame();
        tools.getGlobalGame().sendCountDown(Bukkit.getOnlinePlayers().stream().map(p -> (Player) p).toList());
    }

    @Subcommand("play-start")
    public void start(CommandSender sender) {
        var tools = instance.getGame();
        tools.getGlobalGame().playSquidGameStart();
    }

    @Subcommand("title-black")
    public void title(CommandSender sender, Integer fadeIn, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            instance.showTitle(player, Character.toString('\u3400'), "", fadeIn, 20, fadeOut);
        });

    }

    @Subcommand("kill-bodys")
    public void onBody(Player sender, int i){
        var stands = sender.getLocation().getNearbyEntities(i, i, i).stream()
            .filter(entity -> entity instanceof ArmorStand).map(stand -> (ArmorStand) stand)
            .filter(stand -> (stand.getEquipment().getItemInMainHand() != null 
            && stand.getEquipment().getItemInMainHand().getType() ==  Material.PLAYER_HEAD) || (stand.getEquipment().getItemInOffHand() != null 
            && stand.getEquipment().getItemInOffHand().getType() ==  Material.PLAYER_HEAD)).toList();
        for (var armorStand : stands) {
            armorStand.remove();
        }

    }

    @Subcommand("tp-guards")
    public void onTpGuards(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (instance.getGame().isGuard(p)) {
                p.teleport(player);
            }
        });
    }

    @Subcommand("tp-players")
    public void onTpPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (instance.getGame().isPlayer(p)) {
                p.teleport(player);
            }
        });
    }

}