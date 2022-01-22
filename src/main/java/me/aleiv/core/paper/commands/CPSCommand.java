package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.listeners.CPSListener;
import me.aleiv.core.paper.objects.PlayerClicks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

@CommandAlias("cps")
@CommandPermission("core.cps")
public class CPSCommand extends BaseCommand {

    private final Core instance;
    private final CPSListener cpsListener;

    public CPSCommand(Core instance) {
        this.instance = instance;

        this.cpsListener = new CPSListener(instance);
        this.instance.registerListener(cpsListener);
    }

    @Subcommand("check")
    @CommandCompletion("@players duration")
    @Syntax("<player> [duration]")
    public void getCPS(CommandSender sender, String playerName, @Optional Integer duration) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerClicks pc = cpsListener.getPlayerClicks(target.getUniqueId());
        if (pc == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        if (duration == null) {
            this.sendMessage(target, pc);
        } else {
            AtomicInteger seconds = new AtomicInteger(duration*4);
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendMessage(target, pc);
                    if (seconds.getAndDecrement() == 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(instance, 0, 5);
        }
    }

    @Subcommand("stop")
    public void onStop(CommandSender sender) {
        if (!this.cpsListener.isActive()) {
            sender.sendMessage("§aCPS counter is already inactive.");
            return;
        }

        this.cpsListener.setActive(false);
        sender.sendMessage("§aCPS counter has been stopped.");
    }

    @Subcommand("start")
    public void onStart(CommandSender sender) {
        if (this.cpsListener.isActive()) {
            sender.sendMessage("§aCPS counter is already active.");
            return;
        }

        this.cpsListener.setActive(true);
        sender.sendMessage("§aCPS counter has been started.");
    }

    private void sendMessage(CommandSender sender, PlayerClicks pc) {
        sender.sendMessage("§a" + sender.getName() + "§7's Left Click CPS: §a" + pc.getLeftClicks() + " §7Right Click CPS: §a" + pc.getRightClicks());
    }
}
