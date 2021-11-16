package me.aleiv.core.paper.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.NonNull;
import me.aleiv.core.paper.Core;

@CommandAlias("old-cookie")
@CommandPermission("admin.perm")
public class CookieCMD extends BaseCommand {

    private @NonNull Core instance;

    public CookieCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("door")
    public void door(CommandSender sender, Boolean bool) {
        var tools = instance.getGame().getCookieGame();
        tools.mainDoor(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Cookie main door " + bool);
    }

    @Subcommand("door")
    public void cookieDoor(CommandSender sender, Integer i, Boolean bool) {
        sender.sendMessage(ChatColor.DARK_AQUA + "Cookie door " + i + " " + bool);
        var tools = instance.getGame().getCookieGame();
        switch (i) {
        case 1:
            tools.cookieDoor1(bool);
            break;
        case 2:
            tools.cookieDoor2(bool);
            break;
        case 3:
            tools.cookieDoor3(bool);
            break;
        case 4:
            tools.cookieDoor4(bool);
            break;

        default:
            break;
        }
    }

    /**
     * Give Cookie to user
     */
    @Subcommand("give")
    public void giveToPlayer(CommandSender sender, OnlinePlayer player) {
        var tools = instance.getGame().getCookieGame();
        // tools.giveCookie(player);
        sender.sendMessage(ChatColor.DARK_AQUA + "Cookie given to " + player);
    }

}