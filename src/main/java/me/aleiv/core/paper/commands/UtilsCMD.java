package me.aleiv.core.paper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.Role;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("utils")
@CommandPermission("admin.perm")
public class UtilsCMD extends BaseCommand {

    private @NonNull Core instance;

    public UtilsCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("make-all-sleep")
    public void makeAllSleep(CommandSender sender){
        sender.sendMessage(ChatColor.DARK_AQUA + "Make all sleep");
        var tools = instance.getGame().getMainRoom();
        tools.makeAllSleep();
    }

    @Subcommand("set-spawn")
    public void setSpawn(Player sender){
        sender.sendMessage(ChatColor.DARK_AQUA + "Set Spawn");
        var tools = instance.getGame();
        tools.setCity(sender.getLocation());
    }

    @Subcommand("gas")
    public void sendGas(CommandSender sender){
        var tools = instance.getGame();
        tools.getGlobalGame().applyGas(Bukkit.getOnlinePlayers().stream().map(p -> (Player) p).toList());
    }

    @Subcommand("countdown")
    public void sendCount(CommandSender sender){
        var tools = instance.getGame();
        tools.getGlobalGame().sendCountDown(Bukkit.getOnlinePlayers().stream().map(p -> (Player) p).toList());
    }

    @Subcommand("role-global")
    public void roleGlobal(CommandSender sender, Role role) {
        var game = instance.getGame();
        var roles = game.getRoles();
        
        Bukkit.getOnlinePlayers().forEach(player ->{
            var uuid = player.getUniqueId().toString();
            roles.put(uuid, role);
        });
        
        sender.sendMessage(ChatColor.DARK_AQUA + "Global role set to " + role.toString().toLowerCase());
    }

    


}