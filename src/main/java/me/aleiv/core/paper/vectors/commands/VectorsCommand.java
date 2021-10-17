package me.aleiv.core.paper.vectors.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;

/**
 * A command design to play around with vectors in minecraft.
 * 
 * @author jcedeno
 */
@CommandAlias("vectors|vec")
public class VectorsCommand extends BaseCommand {

    @CommandCompletion("/command1 & /commandn+1 ")
    @CommandAlias("chain0x|c0x")
    @Subcommand("chain-cmd|ccmd")
    public void rotate(Player sender, String args) {
        // Split the commands to be executed by && and execute them
        String[] cmds = args.split("&");
        Bukkit.broadcastMessage("Hola que hace xd  holahola");
        for (String cmd : cmds) {
            var cleanedUpCommand = cmd.trim().substring(1);
            sender.sendMessage(Core.getMiniMessage()
                    .parse(String.format("<green>Executing command <white>%s ", cleanedUpCommand)));
            Bukkit.dispatchCommand(sender, cleanedUpCommand);
        }

    }

}
