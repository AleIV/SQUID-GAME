package me.aleiv.core.paper.commands;

import me.Fupery.ArtMap.ArtMap;
import me.Fupery.ArtMap.Easel.Easel;
import me.aleiv.core.paper.Games.CookieGame;
import me.aleiv.core.paper.objects.CookieCapsule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CommandPermission("admin.perm")
@CommandAlias("cookie")
public class CookieCMD extends BaseCommand {

    private @NonNull Core instance;

    private CookieGame cookieGame;

    public CookieCMD(Core instance) {
        this.instance = instance;

        this.cookieGame = this.instance.getGame().getCookieGame();
        //instance.getCommandManager().getCommandCompletions().registerStaticCompletion("cookies",
                //CookieEnum.getAll().stream().map(m -> m.name()).toList());
    }

    /*@CommandCompletion("@cookies")
    @Subcommand("give")
    public void giveCookie(Player sender, String cookieType) {

        CookieEnum cookie = CookieEnum.valueOf(cookieType.toUpperCase());
        if (cookie == null) {
            sender.sendMessage("Invalid cookie type");
            return;
        }

        var cookieCase = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        var itemMeta = cookieCase.getItemMeta();
        itemMeta.setCustomModelData(cookie.getModelData());

        cookieCase.setItemMeta(itemMeta);

        sender.getInventory().addItem(cookieCase);

    }*/

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

    @Subcommand("start")
    public void start(Player player) {
        if (this.cookieGame.isStarted()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Game already started");
            return;
        }

        // TODO: Pasar las locations
        List<Location> locations = new ArrayList<>();
        locations.add(player.getLocation().clone().add(0, 25, 0));
        locations.add(player.getLocation().clone().add(0, 25, 10));
        locations.add(player.getLocation().clone().add(0, 25, 20));

        this.instance.getGame().getCookieGame().start(locations);
    }

    @Subcommand("add-location")
    public void addLocation(Player player) {
        this.cookieGame.getCapsuleLocations().add(player.getLocation().clone());
        player.sendMessage(ChatColor.DARK_AQUA + "Location added");
    }

    @Subcommand("stop")
    public void stop(Player player) {
        if (!this.cookieGame.isStarted()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Game not started");
            return;
        }

        this.cookieGame.stop();
        player.sendMessage(ChatColor.DARK_AQUA + "Game stopped");
    }

    @Subcommand("test")
    public void test(Player player, boolean bool) {
        CookieCapsule cc = instance.getGame().getCookieGame().getCapsule(player);
        if (bool) {
            cc.mount();
        } else {
            cc.unmount(true);
        }
    }

    @Subcommand("test2")
    public void test2(Player player) {
        instance.getGame().getCookieGame().destroyCapsule(player);
    }

}
