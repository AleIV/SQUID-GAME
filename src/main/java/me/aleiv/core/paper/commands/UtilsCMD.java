package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game;
import me.aleiv.core.paper.listeners.LimitListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("utils")
@CommandPermission("admin.perm")
public class UtilsCMD extends BaseCommand {

    private @NonNull Core instance;
    private LimitListener limitListener;

    public UtilsCMD(Core instance) {
        this.instance = instance;
        this.limitListener = new LimitListener(instance);
        instance.registerListener(limitListener);
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

    @CommandAlias("limit")
    @CommandCompletion("@nothing")
    @Syntax("<limit>")
    public void onLimit(Player player, int i) {
        this.limitListener.setLimit(i);
        this.instance.getGame().setPvp(Game.PvPType.ONLY_PVP);
        player.sendMessage("Limit set to " + i);
    }

    @CommandAlias("countplayers")
    @CommandCompletion("@nothing")
    @Syntax("<range>")
    public void onCountPlayers(Player player, int i) {
        List<Player> players = player.getNearbyEntities(i, i, i).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(p -> this.instance.getGame().isPlayer(p)).toList();
        player.sendMessage("Players in range: " + players.size());
        player.sendMessage("Players: " + players.stream().map(HumanEntity::getName).collect(Collectors.joining(", ")));
    }
    
    @CommandAlias("clear-chat")
    public void onClearChat(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!p.hasPermission("squidgame.bypasschatclear")) {
                this.clearchat(p);
            }
        });

        player.sendMessage("Chat cleared for players without perm 'squidgame.bypasschatclear'");
    }

    private void clearchat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage(ChatColor.BLACK + " ");
        }
    }

    @Subcommand("shufflelocations")
    public void onShuffle(Player player) {
        List<Location> locs = Bukkit.getOnlinePlayers().parallelStream()
                .filter(p -> this.instance.getGame().isPlayer(p))
                .map(Entity::getLocation).collect(Collectors.toList());
        List<Player> players = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getPlayer).toList();
        Collections.shuffle(locs);

        // Teleport a player to each location with a for each loop
        for (int i = 0; i < locs.size(); i++) {
            players.get(i).teleport(locs.get(i));
        }


        player.sendMessage(ChatColor.DARK_AQUA + "Players shuffled");
    }

}
