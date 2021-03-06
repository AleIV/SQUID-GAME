package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.aleiv.cinematicCore.paper.CinematicTool;
import me.aleiv.cinematicCore.paper.objects.NPCInfo;
import me.aleiv.core.paper.Core;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@CommandAlias("guardnpc")
@CommandPermission("admin.perm")
public class GuardnpcCMD extends BaseCommand {

    private @NonNull Core instance;

    public GuardnpcCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("create")
    public void onCreate(Player player) {
        NPCInfo npcInfo = new NPCInfo(player, true, true, true);
        npcInfo.setName(UUID.randomUUID().toString().substring(0, 15));
        npcInfo.setCache(true);
        CinematicTool.getInstance().getNpcManager().spawnNPC(npcInfo);

        player.sendMessage("Guard NPC spawned!");
    }

    @Subcommand("remove")
    @CommandCompletion("@nothing")
    @Syntax("<range>")
    public void removeRange(Player player, int range) {
        new ArrayList<>(CinematicTool.getInstance().getNpcManager().getNPCs()).forEach(npc -> {
            Location loc = npc.getLocation().getLocation();
            if (loc.distance(player.getLocation()) <= range) {
                npc.setCache(false);
                CinematicTool.getInstance().getNpcManager().removeNPC(npc);
            }
        });
        player.sendMessage("NPCs removed");
    }

}
