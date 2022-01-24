package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

@CommandAlias("winner")
@CommandPermission("core.winner")
public class WinnerCMD extends BaseCommand {

    private final Core plugin;
    private final HashMap<String, String> winners;

    public WinnerCMD(Core plugin) {
        this.plugin = plugin;

        this.winners = new HashMap<>();
        this.winners.put("SpreenDMC", "ʔ");
        this.winners.put("Cristinini", "ʕ");
        this.winners.put("BarbeQWTF", "ʖ");
        this.winners.put("lNuvia", "ʗ");
        this.winners.put("aldo_geo", "ʘ");
        this.winners.put("MarkiLokuras", "ʙ");
        this.winners.put("Alecmolon", "̀");
        this.winners.put("AriGameplays", "́");
        this.winners.put("Lakshart", "̂");
        this.winners.put("D3stri", "̃");
        this.winners.put("ElJuaniquilador", "̄");
        this.winners.put("xokas", "̅");
        this.winners.put("MissoSinfo", "̆");
        this.winners.put("RaptorGS", "̇");
        this.winners.put("MrKeroro10", "̈");
        this.winners.put("iLuh", "̉");
        this.winners.put("OllieGamerz", "̐");
        this.winners.put("aXoZer", "̑");
        this.winners.put("Shadoune666", "̒");
        this.winners.put("Robleis01", "̓");
        this.winners.put("iRoier", "̔");
        this.winners.put("LuzuVlogs", "̕");
        this.winners.put("SoyBarcaGamer", "̖");
        this.winners.put("DeqiuV", "̗");
    }

    @Default
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onWinner(CommandSender sender, String playerName) {
        String symbol = this.winners.get(playerName);
        if (symbol == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle("\uF808" + symbol, "§6", 20, 20*10, 20);
            p.playSound(p.getLocation(), "sfx.trumpet_final", 50, 1);
        });
    }


}
