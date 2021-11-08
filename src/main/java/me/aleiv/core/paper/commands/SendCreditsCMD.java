package me.aleiv.core.paper.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.aleiv.core.paper.map.packet.WrapperPlayServerGameStateChange;

@CommandAlias("credits")
public class SendCreditsCMD extends BaseCommand {

    @Default
    public void sendCredits(CommandSender sender, @Flags("other") OnlinePlayer player) {
        // Create packet (src https://wiki.vg/Protocol#Change_Game_State)
        var packet = new WrapperPlayServerGameStateChange();
        // Set reason to 4 (win game)
        packet.setReason(4);
        // Send value to 1 (show end credits)
        packet.setValue(1);
        // Send the credits
        packet.sendPacket(player.getPlayer());

    }

}
