package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.LineVector;
import net.kyori.adventure.text.minimessage.MiniMessage;

@CommandAlias("games")
@CommandPermission("admin.perm")
public class GamesCMD extends BaseCommand {

    private @NonNull Core instance;

    public GamesCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("panel")
    @CommandAlias("panel")
    public void game(Player sender) {
        var game = instance.getGame();
        game.getMainGamePanel().open(sender);

    }

    @Subcommand("greenLight")
    @CommandAlias("greenLight")
    public void greenLight(Player sender, @Name("target") @Optional @Flags("other") Player target) {
        var game = instance.getGame();
        if (target == null) {
            game.getMainGamePanel().getGreenLightPanel().open(sender);
            return;
        } else {
            game.getMainGamePanel().getGreenLightPanel().shoot(target);
            sender.sendMessage(MiniMessage.get().parse("<rainbow>You have shot " + target.getName()));
        }

    }

    @Subcommand("particle-distance")
    public void greenLight(CommandSender sender, @Name("distance") Double distance) {
        LineVector.interval = distance;
        sender.sendMessage("Changed the particle distance to " + LineVector.interval);

    }

    @Subcommand("note-block")
    public void test(Player sender, Boolean bool, String note) {
        var block = sender.getTargetBlock(6);
        if (block != null) {
            AnimationTools.setBlockValue(bool, block.getLocation(), note);
        }

    }

    @Subcommand("test")
    public void test(Player sender, String str) {
        AnimationTools.playSoundDistance(sender.getLocation(), 10, str, 1f, 1f);

    }

}
