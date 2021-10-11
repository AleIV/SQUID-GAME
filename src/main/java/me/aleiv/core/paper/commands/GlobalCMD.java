package me.aleiv.core.paper.commands;

import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("global")
@CommandPermission("admin.perm")
public class GlobalCMD extends BaseCommand {

    private @NonNull Core instance;
    Entity current = null;

    public GlobalCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("game")
    public void game(CommandSender sender, GameType gameType, Boolean bool){
        var game = instance.getGame();
        var games = game.getGames();
        if(!games.containsKey(gameType)){
            sender.sendMessage(ChatColor.RED + "Game type doesn't exist.");
            
        }else{

            games.put(gameType, bool);
            sender.sendMessage(ChatColor.DARK_AQUA + "Game type " + gameType + " " + bool + ".");
        }
    }

    @Subcommand("particle")
    public void particle(Player sender, Particle particle, double x, double y, double z, Integer count, double speed){
        var loc = sender.getLocation();
        var world = loc.getWorld();
        //world.spawnParticle(Particle.TOTEM, loc, x, y, z, count, speed);
        world.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), count, x, y, z, speed, null, false);
        

    }

    @Subcommand("move")
    public void move(Player sender, Integer value, Integer tickSpeed, char pos){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            AnimationTools.move(stand, value, tickSpeed, pos);

        }
    }

    @Subcommand("rotate")
    public void rotate(Player sender, Integer value, Integer tickSpeed){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            AnimationTools.rotate(stand, value, tickSpeed);

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
