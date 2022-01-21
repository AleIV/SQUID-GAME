package me.aleiv.core.paper.Games.potato;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

@CommandAlias("potato")
@CommandPermission("admin.perm")
public class PotatoCMD extends BaseCommand {

    private @NonNull Core instance;

    public PotatoCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("explode")
    public void explode(CommandSender sender){

        Bukkit.getOnlinePlayers().forEach(player ->{
            var loc = player.getLocation();
            player.playSound(loc, "squid:sfx.potato_explode", 1111, 1);
        });
        Bukkit.getScheduler().runTaskLater(instance, task->{

            Bukkit.getOnlinePlayers().forEach(player ->{
                var inv = player.getInventory();
                if(inv.contains(Material.RABBIT_FOOT)){
                    inv.remove(Material.RABBIT_FOOT);
                    player.getLocation().createExplosion(3, false, false);
                }
            });

        }, 20*10);
    }

}
