package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;

@CommandAlias("games")
@CommandPermission("admin.perm")
public class GamesCMD extends BaseCommand {

    private @NonNull Core instance;

    public GamesCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("panel")
    public void game(CommandSender sender) {
        // var game = instance.getGame();

    }

    @CommandCompletion("[float]")
    @Subcommand("test")
    public void test(Player sender, @Default("30") Float speed) {
        AnimationTools.shootLocation(sender.getLocation(), speed);
    }

}
