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
import me.aleiv.core.paper.objects.LineVector;
import me.aleiv.core.paper.objects.RopeGame;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("test")
@CommandPermission("admin.perm")
public class TestCMD extends BaseCommand {

    private @NonNull Core instance;

    public TestCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("panel")
    @CommandAlias("panel")
    public void game(Player sender) {
        var game = instance.getGame();
        game.getMainGamePanel().open(sender);

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

    @Subcommand("bossbar")
    public void bossbar(Player sender, Integer neg, Integer neg2) {
        switch (neg) {
            case 1 -> {
                instance.broadcastMessage(ChatColor.RED + "1 "+ RopeGame.neg1);
                RopeGame.neg1 = neg2;
                instance.broadcastMessage(ChatColor.GREEN + "1 "+ RopeGame.neg1);
            }
            case 2 -> {
                instance.broadcastMessage(ChatColor.RED + "2 "+ RopeGame.neg1);
                RopeGame.neg2 = neg2;
                instance.broadcastMessage(ChatColor.GREEN  + "2 "+ RopeGame.neg2);
            }
            case 3 -> {
                instance.broadcastMessage(ChatColor.RED + "3 "+ RopeGame.neg3);
                RopeGame.neg3 = neg2;
                instance.broadcastMessage(ChatColor.GREEN + "3 "+ RopeGame.neg3);
            }
            case 4 -> {
                instance.broadcastMessage(ChatColor.RED + "4 "+ RopeGame.neg4);
                RopeGame.neg4 = neg2;
                instance.broadcastMessage(ChatColor.GREEN + "4 " + RopeGame.neg4);
            }
        
        }
        instance.getGame().getRopeGame().updateBossBar();


    }

}
