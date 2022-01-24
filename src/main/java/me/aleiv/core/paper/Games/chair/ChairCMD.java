package me.aleiv.core.paper.Games.chair;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        Location startingLoc = new Location(Bukkit.getWorld("world"), 356, 42, -145);
        ArrayList<Location> locs = this.getBlockRadius(startingLoc.clone(), 16).parallelStream().map(Block::getLocation).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        for (int j = 0; j < i; j++) {
            int randomIndex = random.nextInt(locs.size()-1);
            Location loc = locs.remove(randomIndex);
            spawnChair(loc);
        }
    }

    private void spawnChair(Location loc) {
        var stand = AnimationTools.getFormattedStand(loc.getWorld(), loc);
        stand.setSmall(true);
        AnimationTools.setStandModel(stand, Material.BRICK, 40);
    }

    private List<Block> getBlockRadius(Location loc, int radius){
        if (radius < 0) {
            return new ArrayList<>();
        }
        Block start = loc.getBlock();
        List<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block block = start.getRelative(x, 0, z);
                blocks.add(block);
            }
        }
        return blocks;
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
