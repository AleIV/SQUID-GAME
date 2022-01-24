package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.Participant.Role;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("main")
@CommandPermission("admin.perm")
public class MainCMD extends BaseCommand {

    private @NonNull Core instance;

    public MainCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("lights")
    @CommandCompletion("@bool")
    public void game(CommandSender sender, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Lights " + bool);

        var tools = instance.getGame().getMainRoom();
        tools.lights(bool);
    }

    @Subcommand("pasiveLights")
    @CommandCompletion("@bool")
    public void pasiveLights(CommandSender sender, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Pasive Lights " + bool);

        var tools = instance.getGame().getMainRoom();
        tools.pasiveLights(bool);
    }

    @Subcommand("elevator")
    @CommandCompletion("@bool")
    public void elevator(CommandSender sender, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Main elevator " + bool);
        var tools = instance.getGame().getMainRoom();
        tools.mainElevator(bool);
    }

    @Subcommand("submarine-left")
    @CommandCompletion("@bool")
    public void leftDoor(CommandSender sender, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Main left door submarine " + bool);
        var tools = instance.getGame().getMainRoom();
        tools.mainLeftDoor(bool);
    }

    @Subcommand("submarine-right")
    @CommandCompletion("@bool")
    public void rightDoor(CommandSender sender, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Main right door submarine " + bool);
        var tools = instance.getGame().getMainRoom();
        tools.mainRightDoor(bool);
    }

    @Subcommand("tube")
    @CommandCompletion("@bool")
    public void tube(CommandSender sender, Boolean bool) {
        var tools = instance.getGame().getMainRoom();
        tools.moveTube(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Tube move " + bool);
    }

    @Subcommand("screen-turn")
    @CommandCompletion("@bool")
    public void screen(CommandSender sender, Boolean bool) {
        var tools = instance.getGame().getMainRoom();
        tools.turnScreen(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Screen turn " + bool);
    }

    @Subcommand("manual-prize")
    public void refreshPrize(CommandSender sender, Integer newPrize, Integer delay, Integer value) {
        var tools = instance.getGame().getMainRoom();
        tools.refreshPrize(newPrize, delay, value);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed prize " + newPrize + " " + delay);
    }

    @Subcommand("prize")
    public void prize(CommandSender sender, Integer newPrize, Integer count) {
        var tools = instance.getGame().getMainRoom();

        tools.refreshPrize(newPrize, 2, count);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed prize " + newPrize);
    }

    @Subcommand("refresh-players")
    public void players(CommandSender sender, Integer delay) {
        var game = instance.getGame();
        var tools = game.getMainRoom();
        var participants = game.getParticipants().values();
        var totalParticipants = participants.stream().filter(p -> p.getRole() == Role.PLAYER).toList();
        var deathPlayers = totalParticipants.stream().filter(p -> p.isDead()).toList();
        var newValue = totalParticipants.size() - deathPlayers.size();
        tools.refreshPlayers(newValue, delay, 1);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed players ");
    }

    @Subcommand("manual-players")
    public void refreshPlayers(CommandSender sender, Integer newPrize, Integer delay, Integer value) {
        var tools = instance.getGame().getMainRoom();
        tools.refreshPlayers(newPrize, delay, value);
        sender.sendMessage(ChatColor.DARK_AQUA + "Refreshed players " + newPrize + " " + delay);
    }

    @Subcommand("snowball")
    @CommandCompletion("@bool")
    public void snowball(CommandSender sender, Boolean bool) {
        if (bool) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                var inv = p.getInventory().getContents();
                var count = 0;
                for (var item : inv) {
                    if (item != null && item.getType() == Material.GLASS_BOTTLE) {
                        p.getInventory().setItem(count, new ItemStack(Material.SNOWBALL));
                    }
                    count++;
                }

            });

        } else {
            Bukkit.getOnlinePlayers().forEach(p -> {
                var inv = p.getInventory().getContents();
                var count = 0;
                for (var item : inv) {
                    if (item != null && item.getType() == Material.SNOWBALL) {
                        p.getInventory().setItem(count, new ItemStack(Material.GLASS_BOTTLE));
                    }
                    count++;
                }

            });
        }
        sender.sendMessage(ChatColor.DARK_AQUA + "Snowballs " + bool);
    }

    @Subcommand("pass-night")
    public void makeAllSleep(CommandSender sender) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            instance.showTitle(player, Character.toString('\u3400'), "", 100, 20, 100);
        });

        Bukkit.getScheduler().runTaskLater(instance, task -> {
            var game = instance.getGame();
            var beds = AnimationTools.findLocations("BED");
            var guardBeds = AnimationTools.findLocations("GUARDB");
            var players = Bukkit.getOnlinePlayers();
            var participants = players.stream().filter(player -> game.isPlayer(player)).map(player -> (Player) player)
                    .toList();
            var guards = players.stream().filter(player -> game.isGuard(player)).map(player -> (Player) player)
                    .toList();

            AnimationTools.forceSleepInstant(guards, guardBeds);
            AnimationTools.forceSleepInstant(participants, beds);

        }, 110);
    }

}
