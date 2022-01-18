package me.aleiv.core.paper.Games.rope;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("rope")
@CommandPermission("admin.perm")
public class RopeCMD extends BaseCommand {

    private @NonNull Core instance;

    public RopeCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("guillotine")
    @CommandCompletion("@bool")
    public void guillotine(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getRopeGame();
        var task = tools.moveGuillotine(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "Guillotine move " + bool);

        if(!bool){
            task.thenAccept(action ->{
                Bukkit.getScheduler().runTask(instance, tk ->{
                    var ropeEntities1 = tools.getLeftRope();
                    ropeEntities1.forEach(stand -> {
                    AnimationTools.setStandModel(stand, Material.BRICK, 38);

                    });

                    var ropeEntities2 = tools.getRightRope();
                    ropeEntities2.forEach(stand -> {
                    AnimationTools.setStandModel(stand, Material.BRICK, 38);
                    });

                    AnimationTools.setStandModel(AnimationTools.getArmorStand("ROPE_GUILLOTINE"), Material.BRICK, 39);

                });
            });
        }
    }

    @Subcommand("boolMode")
    @CommandCompletion("@bool")
    public void boolMode(CommandSender sender, Boolean bool){
        var tools = instance.getGame().getRopeGame();
        tools.setBoolMode(bool);
        sender.sendMessage(ChatColor.DARK_AQUA + "BoolMode move " + bool);
        
    }

    @Subcommand("gate")
    @CommandCompletion("@bool")
    public void gate(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Rope gate " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.ropeGate(bool);
    }

    @Subcommand("ingame")
    @CommandCompletion("@bool")
    public void play(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Rope ingame " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.setInGame(bool);
    }

    @Subcommand("bossbar")
    @CommandCompletion("@bool")
    public void bossbar(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Bossbar rope " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.enableRope(bool);

        if(tools.getRightRope().isEmpty() || tools.getLeftRope().isEmpty()){
            tools.initRope();
        }
    }

    @Subcommand("points")
    public void points(CommandSender sender, Integer i){
        sender.sendMessage(ChatColor.DARK_AQUA + "Bossbar points rope " + i);
        var tools = instance.getGame().getRopeGame();
        tools.setPoints(i);
        tools.updateBossBar();
    }

    @Subcommand("multiplier")
    public void multiplier(CommandSender sender, Integer i){
        sender.sendMessage(ChatColor.DARK_AQUA + "Bossbar multiplier points rope " + i);
        var tools = instance.getGame().getRopeGame();
        tools.setMultiplier(i);
    }

    @Subcommand("right-elevator")
    @CommandCompletion("@bool")
    public void rightElevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Right elevator " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.rightElevator(bool);
    }

    @Subcommand("left-elevator")
    @CommandCompletion("@bool")
    public void leftElevator(CommandSender sender, Boolean bool){
        sender.sendMessage(ChatColor.DARK_AQUA + "Left elevator " + bool);
        var tools = instance.getGame().getRopeGame();
        tools.leftElevator(bool);
    }

    List<String> numbers = List.of(Character.toString('\u0274') + "", Character.toString('\u0275') + "", Character.toString('\u0276') + "", 
    Character.toString('\u0277') + "", Character.toString('\u0278') + "", Character.toString('\u0279') + "");
    
    @Subcommand("sort")
    public void sort(CommandSender sender, Integer n){
        var random = new Random();
        var task = new BukkitTCT();

        var v = 10;
        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    var num = numbers.get(random.nextInt(numbers.size()));
                    Bukkit.getOnlinePlayers().forEach(p ->{
                        var loc = p.getLocation();
                        instance.showTitle(p, ChatColor.RED + num, "", 0, 80, 0);
                        p.playSound(loc, "squid:sfx.right", 1, 0.1f);
                    });
                }
            }, 50 * 10);
        }

        task.addWithDelay(new BukkitRunnable() {
            @Override
            public void run(){
                var num = numbers.get(n-1);
                Bukkit.getOnlinePlayers().forEach(p ->{
                    var loc = p.getLocation();
                    instance.showTitle(p, num, "", 0, 120, 0);
                    p.playSound(loc, "squid:sfx.right", 1, 0.1f);
                });
            }
        }, 50 * 10);

        task.execute();

    }

}
