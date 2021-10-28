package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.LineVector;

@CommandAlias("test")
@CommandPermission("admin.perm")
public class TestCMD extends BaseCommand {

    private @NonNull Core instance;

    public TestCMD(Core instance) {
        this.instance = instance;
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

    @Subcommand("playsound")
    public void test(Player sender, String str) {
        AnimationTools.playSoundDistance(sender.getLocation(), 10, str, 1f, 1f);

    }

}
