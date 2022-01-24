package me.aleiv.core.paper.Games.glass;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("glass")
@CommandPermission("admin.perm")
public class GlassCMD extends BaseCommand {

    private @NonNull Core instance;

    public GlassCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("break-all")
    public void glass(CommandSender sender){
        var tools = instance.getGame().getGlassGame();
        tools.breakAll();
        sender.sendMessage(ChatColor.DARK_AQUA + "BREAK ALL GLASS");
    }

    @Subcommand("jacket add")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void addJacket(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        instance.getGame().getGlassGame().addPlayerWithJacket(player.getUniqueId());
        sender.sendMessage(ChatColor.DARK_AQUA + "Added " + player.getName() + " to players with jacket");
    }

    @Subcommand("jacket remove")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void removeJacket(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        instance.getGame().getGlassGame().removePlayerWithJacket(player.getUniqueId());
        sender.sendMessage(ChatColor.DARK_AQUA + "Removed " + player.getName() + " to players with jacket");
    }

    @Subcommand("glass-break")
    @CommandCompletion("@players true|false")
    @Syntax("<player> <true|false>")
    public void glassBreak(CommandSender sender, String playerName, @Optional @Default("true") Boolean bool) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        if (bool) {
            instance.getGame().getGlassGame().addPlayerGlassBreak(player.getUniqueId());
        } else {
            instance.getGame().getGlassGame().removePlayerGlassBreak(player.getUniqueId());
        }
        sender.sendMessage(ChatColor.DARK_AQUA + "Set " + player.getName() + " glass break to " + bool);
    }

    @Subcommand("glass-break-force")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void glassBreakFroce(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        instance.getGame().getGlassGame().forceGlassBreak(player);
        sender.sendMessage(ChatColor.DARK_AQUA + "Forced a glass check for " + player.getName());
    }

}