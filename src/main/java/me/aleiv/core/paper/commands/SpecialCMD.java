package me.aleiv.core.paper.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("special")
@CommandPermission("admin.perm")
public class SpecialCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public SpecialCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("add-entity")
    public void addE(Player sender, String name){
        var specialObjects = AnimationTools.specialObjects;
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            if(specialObjects.containsKey(name)){
                sender.sendMessage(ChatColor.RED + "Special object already exist.");
                
            }else{
    
                specialObjects.put(name, entity.getUniqueId().toString());
                sender.sendMessage(ChatColor.DARK_AQUA + "Special object " + name + " added.");
            }

        }else{
            sender.sendMessage(ChatColor.RED + "You don't have target.");
        }

    }

    @Subcommand("add-block")
    public void addB(Player sender, String name){
        var specialObjects = AnimationTools.specialObjects;
        var block = sender.getTargetBlock(5);

        if(block != null){

            if(specialObjects.containsKey(name)){
                sender.sendMessage(ChatColor.RED + "Special object already exist.");
                
            }else{
    
                var loc = block.getLocation();
                specialObjects.put(name, loc.getX() + ";" + loc.getY() + ";" + loc.getZ());
                sender.sendMessage(ChatColor.DARK_AQUA + "Special object " + name + " added.");
            }

        }else{
            sender.sendMessage(ChatColor.RED + "You don't have target.");
        }

    }

    @Subcommand("delete")
    public void move(CommandSender sender, String name){
        var specialObjects = AnimationTools.specialObjects;

        if(!specialObjects.containsKey(name)){
            sender.sendMessage(ChatColor.RED + "Special object doesn't exist.");
            
        }else{

            specialObjects.remove(name);
            sender.sendMessage(ChatColor.DARK_AQUA + "Special object " + name + " deleted.");
        }

    }

    @Subcommand("list")
    public void list(CommandSender sender, @Optional String name){
        var specialObjects = AnimationTools.specialObjects;

        if(name.equalsIgnoreCase("all")){
            sender.sendMessage(ChatColor.DARK_AQUA + "SpecialObjects: " + ChatColor.WHITE + specialObjects.toString());
            
        }else{

            var list = specialObjects.entrySet().stream().filter(entry -> entry.getKey().contains(name)).toList();
            sender.sendMessage(ChatColor.DARK_AQUA + name + " SpecialObjects: " + ChatColor.WHITE + list.toString());
        }

    }

    @Subcommand("move")
    public void move(CommandSender sender, String name, Integer value, Integer tickSpeed, char pos, Float distance){

        var specialObjects = AnimationTools.specialObjects;

        if(!specialObjects.containsKey(name)){
            sender.sendMessage(ChatColor.RED + "Special object doesn't exist.");

        }else{
        
            AnimationTools.move(name, value, tickSpeed, pos, distance);
        }

    }

    @Subcommand("rotate")
    public void rotate(CommandSender sender, String name, Integer value, Integer tickSpeed){

        var specialObjects = AnimationTools.specialObjects;

        if(!specialObjects.containsKey(name)){
            sender.sendMessage(ChatColor.RED + "Special object doesn't exist.");
        }else{
            var uuid = UUID.fromString(specialObjects.get(name));
            var stand = (ArmorStand) Bukkit.getWorld("world").getEntity(uuid);
            AnimationTools.rotate(stand, value, tickSpeed);
        }
    }

    @Subcommand("info")
    public void info(Player sender){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){
            var loc = stand.getLocation();
            instance.broadcastMessage(ChatColor.GREEN + "" + loc.getX() + " " + loc.getY() + " " + loc.getZ());

        }
    }

    @Subcommand("sleep")
    public void sleep(Player sender){
       var loc = sender.getLocation();
       var block = loc.getBlock().getRelative(BlockFace.DOWN);
       if(block.getType().toString().contains("_BED")){
            AnimationTools.forceSleep(sender, block.getLocation());
       }
    }

    @Subcommand("set")
    public void set(Player sender, Float value){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            var part = stand.getHeadPose();
            var x = part.getX();
            var y = part.getY();
            var z = part.getZ();

            stand.setHeadPose(new EulerAngle(x, value, z));
            sender.sendMessage(ChatColor.GREEN + "CHANGE X: " + x + "Y: " + y + "Z: " + z);

        }
    }

}
