package me.aleiv.core.paper.commands;

import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
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

    @Subcommand("stand")
    public void stand(Player sender, String pos, Integer add){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            var task = new BukkitTCT();

            for (int i = 0; i < add; i++) {
                
                task.addWithDelay(new BukkitRunnable(){
                    @Override
                    public void run() {
                        var part = stand.getHeadPose();
                        var x = part.getX();
                        var y = part.getY();
                        var z = part.getZ();
                        var v = x+1 > 360 ? x+1 -360 : x+1;
                        switch (pos) {
                            case "x": stand.setHeadPose(new EulerAngle(v, y, z)); break;
                            case "y":  stand.setHeadPose(new EulerAngle(x, v, z)); break;
                            case "z":  stand.setHeadPose(new EulerAngle(x, y, v)); break;
                            default: break;
                        }
                        sender.sendMessage(ChatColor.GREEN + "CHANGE X: " + x + "Y: " + y + "Z: " + z);
                    }
                }, 50*2);
            }

            task.execute();

        }
    }

    @Subcommand("rotate")
    public void rotate(Player sender, Integer add){
        var entity = sender.getTargetEntity(5);

        if(entity != null && entity instanceof ArmorStand stand){

            var task = new BukkitTCT();

            var v = Math.abs(add);
            for (int i = 0; i < v; i++) {
                
                task.addWithDelay(new BukkitRunnable(){
                    @Override
                    public void run() {
                        var part = stand.getHeadPose();
                        var x = part.getX();
                        var y = part.getY();
                        var z = part.getZ();
                        var v = add < 0 ? y-0.1 : y+0.1;

                        stand.setHeadPose(new EulerAngle(x, v, z));
                        sender.sendMessage(ChatColor.GREEN + "CHANGE X: " + x + "Y: " + y + "Z: " + z);
                    }
                }, 50*2);
            }

            task.execute();

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
