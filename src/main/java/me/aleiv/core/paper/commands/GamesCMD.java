package me.aleiv.core.paper.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
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
    @CommandAlias("panel")
    public void game(Player sender){
        var game = instance.getGame();
        game.getMainGamePanel().open(sender);

    }

    @Subcommand("greenLight")
    @CommandAlias("greenLight")
    public void greenLight(Player sender){
        var game = instance.getGame();
        game.getMainGamePanel().getGreenLightPanel().open(sender);

    }


    @Subcommand("note-block")
    public void test(Player sender, Boolean bool, String note){
        var block = sender.getTargetBlock(6);
        if(block != null){
            AnimationTools.setBlockValue(bool, block.getLocation(), note);
        }

    }

    @Subcommand("test")
    public void test(Player sender, String str){
        AnimationTools.playSoundDistance(sender.getLocation(), 10, str, 1f, 1f);

    }

}
