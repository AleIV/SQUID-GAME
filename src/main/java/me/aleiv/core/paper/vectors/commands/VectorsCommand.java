package me.aleiv.core.paper.vectors.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;

/**
 * A command design to play around with vectors in minecraft.
 * 
 * @author jcedeno
 */
@CommandAlias("vectors:vec")
public class VectorsCommand extends BaseCommand {

    @CommandCompletion("[/command && /command]")
    @CommandAlias("chain0x")
    @Subcommand("chain-cmd:ccmd")
    public void rotate(Player sender, String args) {
        // Split the commands to be executed by && and execute them
        String[] cmds = args.split("&&");
        for (String cmd : cmds) {
            var cleanedUpCommand = cmd.trim().substring(1);
            Bukkit.dispatchCommand(sender, cleanedUpCommand);
        }

    }

}
