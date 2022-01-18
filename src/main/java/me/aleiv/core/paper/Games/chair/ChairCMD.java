package me.aleiv.core.paper.Games.chair;

import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("chair|disco")
@CommandPermission("admin.perm")
public class ChairCMD extends BaseCommand {

    private @NonNull Core instance;

    public ChairCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("notes|music")
    @CommandCompletion("@bool")
    public void door(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getChairGame();
        tools.turnMusic(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "MUSIC TURN " + bool);
    }

    @Subcommand("kill-chairs")
    public void chairsKill(Player sender, Integer i){
        var stands = sender.getLocation().getNearbyEntities(i, i, i).stream()
            .filter(entity -> entity instanceof ArmorStand).map(stand -> (ArmorStand) stand)
            .filter(stand -> stand.getEquipment().getHelmet() != null).collect(Collectors.toList());

        var chairs = stands.stream().filter(stand -> stand.getEquipment().getHelmet().hasItemMeta() && stand.getEquipment().getHelmet().getItemMeta().hasCustomModelData()
        && stand.getEquipment().getHelmet().getItemMeta().getCustomModelData() == 40).toList();

        for (var armorStand : chairs) {
            armorStand.remove();
            sender.sendMessage(ChatColor.DARK_AQUA + "REMOVED CHAIR");
        }
    }


    @Subcommand("place-chair")
    public void onBody(Player sender, int i){
        var world = sender.getWorld();
        var loc = sender.getLocation();
        var random = new Random();
        
        for (int j = 0; j < i; j++) {
            var bool = random.nextBoolean() ? -1 : 1;
            var x = bool * random.nextInt(6);
            var z = bool * random.nextInt(6);
            var stand = AnimationTools.getFormattedStand(world, loc.add(x, 0, z));
            stand.setSmall(true);
            AnimationTools.setStandModel(stand, Material.BRICK, 40);
        }
        

    }
}
