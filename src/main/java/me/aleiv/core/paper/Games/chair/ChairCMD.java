package me.aleiv.core.paper.Games.chair;

import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
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

    Random random = new Random();

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
        var location = sender.getLocation();
        
        for (int j = 0; j < i; j++) {

            var loc = findScatterLocation(location, 5, 5);
            var stand = AnimationTools.getFormattedStand(world, loc);
            stand.setSmall(true);
            AnimationTools.setStandModel(stand, Material.BRICK, 40);
        }
        

    }

    public static Location findScatterLocation(Location loc, final int radius, int max) {
        // Use Math#Random to obtain a random integer that can be used as a location.
        loc.setX(loc.getX() + Math.random() * radius);
        loc.setZ(loc.getZ() + Math.random() * radius);

        // A location object is returned once we reach this step, next step is to
        // validate the location from others.
        return loc;
    }

    public int getR(int i) {
        var neg = random.nextBoolean();
        var rand = random.nextInt(i) + 1;
        return neg ? rand * 1 : rand * -1;
    }

    public Location genLoc(Location location, Integer i) {
        var world = location.getWorld();
        var loc = new Location(world, getR(i), location.getY(), getR(i));
        return loc;
    }
}
