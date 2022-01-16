package me.aleiv.core.paper.Games.cookie;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.gui.CookieGUI;
import me.aleiv.core.paper.objects.CookieCapsule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandPermission("admin.perm")
@CommandAlias("cookie")
public class CookieCMD extends BaseCommand {

    private @NonNull Core instance;

    private CookieGame cookieGame;

    public CookieCMD(Core instance) {
        this.instance = instance;

        this.cookieGame = this.instance.getGame().getCookieGame();
        // instance.getCommandManager().getCommandCompletions().registerStaticCompletion("cookies",
        // CookieEnum.getAll().stream().map(m -> m.name()).toList());

    }

    /*
     * @CommandCompletion("@cookies")
     * 
     * @Subcommand("give")
     * public void giveCookie(Player sender, String cookieType) {
     * 
     * CookieEnum cookie = CookieEnum.valueOf(cookieType.toUpperCase());
     * if (cookie == null) {
     * sender.sendMessage("Invalid cookie type");
     * return;
     * }
     * 
     * var cookieCase = new ItemStack(Material.FERMENTED_SPIDER_EYE);
     * var itemMeta = cookieCase.getItemMeta();
     * itemMeta.setCustomModelData(cookie.getModelData());
     * 
     * cookieCase.setItemMeta(itemMeta);
     * 
     * sender.getInventory().addItem(cookieCase);
     * 
     * }
     */

    @Subcommand("door")
    @CommandCompletion("@bool")
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

        List<Location> locations = AnimationTools.findLocations("COOKIEBOX");

        this.instance.getGame().getCookieGame().start(locations);
    }

    @Subcommand("fails")
    @CommandCompletion("@players")
    @Syntax("<Player>")
    public void menuFails(Player sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule cookieCapsule = this.cookieGame.getCapsule(player);
        if (cookieCapsule == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }

        sender.sendMessage(ChatColor.DARK_AQUA + "Player " + player.getName() + " has " + cookieCapsule.getErrors() + " errors");
    }

    @Subcommand("winner")
    @CommandCompletion("@players")
    @Syntax("<Player>")
    public void menuWinners(Player sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule cookieCapsule = this.cookieGame.getCapsule(player);
        if (cookieCapsule == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }

        sender.sendMessage(ChatColor.DARK_AQUA + "Player " + player.getName() + " has " + (cookieCapsule.isDone() ? "finished" : "not finished"));
    }

    @Subcommand("menu")
    public void menu(Player sender) {
        new CookieGUI(sender, CookieGUI.Filter.NONE);
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

    @Subcommand("block")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onBlock(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }
        if (capsule.isBlocked()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player already blocked");
            return;
        }
        capsule.block();
        player.sendMessage(ChatColor.DARK_AQUA + "Player blocked");
    }

    @Subcommand("blockall")
    public void onBlockAll(Player player) {
        this.cookieGame.getAllCapules().forEach(CookieCapsule::block);
        player.sendMessage(ChatColor.DARK_AQUA + "All players blocked");
    }

    @Subcommand("unblockall")
    public void onUnblockAll(Player player) {
        this.cookieGame.getAllCapules().forEach(c -> {
            if (!c.isDone()) {
                c.unblock();
            }
        });
        player.sendMessage(ChatColor.DARK_AQUA + "All players that didn't win already unblocked");
    }

    @Subcommand("unblock")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onUnblock(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }
        if (!capsule.isBlocked()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player isn't blocked");
            return;
        }
        capsule.unblock();
        player.sendMessage(ChatColor.DARK_AQUA + "Player unblocked");
    }

    @Subcommand("force skip")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onForceSkip(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }

        if (capsule.isMounted()) {
            capsule.block();
        }

        this.cookieGame.skip(target);
        player.sendMessage(ChatColor.DARK_AQUA + "Player capsule skipped. Create a new one");
    }

    @Subcommand("force mount")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onForceMount(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }
        if (capsule.isMounted()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player already mounted");
            return;
        }
        capsule.mount();
        player.sendMessage(ChatColor.DARK_AQUA + "Player mounted");
    }

    @Subcommand("force unmount")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onForceUnmount(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }
        if (!capsule.isMounted()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player isn't mounted");
            return;
        }
        capsule.unmount(true);
        player.sendMessage(ChatColor.DARK_AQUA + "Player unmounted");
    }

    @Subcommand("force unmountNoEasel")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onForceUnmountNoEasel(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }
        if (!capsule.isMounted()) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player isn't mounted");
            return;
        }
        capsule.unmount(false);
        player.sendMessage(ChatColor.DARK_AQUA + "Player unmounted");
    }

    @Subcommand("force create")
    @CommandCompletion("@players @cookieTypes")
    @Syntax("<player> <cookieType>")
    public void onForceCreate(Player player, String playerName, String cookieType) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieGame.CookieType type;
        try {
            type = CookieGame.CookieType.valueOf(cookieType.toUpperCase());
        } catch (Exception e) {
            type = null;
        }
        if (type == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Invalid cookie type");
            return;
        }

        this.cookieGame.createCapsule(target, type);
        player.sendMessage(ChatColor.DARK_AQUA + "Capsule forced successfully");
    }

    @Subcommand("force win")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onForceWin(Player player, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player not found");
            return;
        }

        CookieCapsule capsule = this.cookieGame.getCapsule(target);
        if (capsule == null) {
            player.sendMessage(ChatColor.DARK_AQUA + "Player doesn't have a capsule");
            return;
        }

        capsule.win();
        player.sendMessage(ChatColor.DARK_AQUA + "Player won");
    }

}
