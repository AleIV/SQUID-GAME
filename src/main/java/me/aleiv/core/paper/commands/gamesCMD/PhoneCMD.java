package me.aleiv.core.paper.commands.gamesCMD;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.modeltool.exceptions.AlreadyUsedNameException;
import me.aleiv.modeltool.exceptions.InvalidModelIdException;
import me.aleiv.modeltool.models.EntityMood;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("phone")
@CommandPermission("admin.perm")
public class PhoneCMD extends BaseCommand {

    private @NonNull Core instance;

    public PhoneCMD(Core instance){
        this.instance = instance;
    }

    @Subcommand("sound")
    public void sound(CommandSender sender){
        var stand = AnimationTools.getArmorStand("PHONE");
        var loc = stand.getLocation();
        AnimationTools.playSoundDistance(loc, 100, "squid:sfx.ringing_phone", 1, 1);
        AnimationTools.setStandModel(stand, Material.BRICK, 59);
        Bukkit.getScheduler().runTaskLater(instance, task ->{
            AnimationTools.setStandModel(stand, Material.BRICK, 58);
        }, 20*3);

        sender.sendMessage(ChatColor.DARK_AQUA + "Phone ringing");
    }

    @Subcommand("disguise")
    public void disguise(Player player, boolean bool){
        var manager = instance.getEntityModelManager();
        
        if(bool){
            
            try {
                var loc = player.getLocation();
                var entity = manager.spawnEntityModel(UUID.randomUUID().toString(), 20, "pug", loc, EntityType.WOLF, EntityMood.NEUTRAL);
                manager.disguisePlayer(player, entity);

            } catch (InvalidModelIdException | AlreadyUsedNameException e) {
                e.printStackTrace();
            }
            
        }else{
            manager.undisguisePlayer(player);
            
        }
    }
}
