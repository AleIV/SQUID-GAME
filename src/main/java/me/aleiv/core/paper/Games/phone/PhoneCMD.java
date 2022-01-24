package me.aleiv.core.paper.Games.phone;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.ticxo.modelengine.api.generator.blueprint.Animation;
import lombok.NonNull;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.modeltool.core.EntityModel;
import me.aleiv.modeltool.exceptions.AlreadyUsedNameException;
import me.aleiv.modeltool.exceptions.InvalidAnimationException;
import me.aleiv.modeltool.exceptions.InvalidModelIdException;
import me.aleiv.modeltool.models.EntityMood;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

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
    @CommandCompletion("@bool")
    public void disguise(Player player, boolean bool){
        var manager = instance.getEntityModelManager();
        
        if(bool){
            if (manager.isPlayerDisguised(player)) {
                player.sendMessage(ChatColor.RED + "You are already disguised");
                return;
            }
            try {
                var loc = player.getLocation();
                var entity = manager.spawnEntityModel(UUID.randomUUID().toString(), 20, "pug", loc, EntityType.WOLF, EntityMood.STATIC);
                manager.disguisePlayer(player, entity);

            } catch (InvalidModelIdException | AlreadyUsedNameException e) {
                e.printStackTrace();
            }

        }else{
            EntityModel model = manager.getEntityModel(player.getUniqueId());
            if (model == null) {
                player.sendMessage(ChatColor.RED + "You are not disguised");
                return;
            }

            manager.undisguisePlayer(player);
            model.remove();
        }
    }

    @Subcommand("rubius disguise")
    public void onRubiusDisguise(Player player) {
        var manager = instance.getEntityModelManager();

        if (manager.isPlayerDisguised(player)) {
            player.sendMessage(ChatColor.RED + "You are already disguised");
            return;
        }

        try {
            var loc = player.getLocation();
            var entity = manager.spawnEntityModel(UUID.randomUUID().toString(), 20, "rubius", loc, EntityType.WOLF, EntityMood.STATIC);
            manager.disguisePlayer(player, entity);

        } catch (InvalidModelIdException | AlreadyUsedNameException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("rubius undisguise")
    public void onRubiusUndisguise(Player player) {
        var manager = instance.getEntityModelManager();
        EntityModel model = manager.getEntityModel(player.getUniqueId());
        if (model != null) {
            manager.undisguisePlayer(player);
            model.remove();
        }
    }

    @Subcommand("redetect")
    public void onRubiusRecheck(Player player) {
        var manager = instance.getEntityModelManager();
        EntityModel model = manager.getEntityModel(player.getUniqueId());
        if (model != null) {
            player.sendMessage(ChatColor.GREEN + "You are not in a model.");
            return;
        }

        model.getModeledEntity().detectPlayers();
        player.sendMessage(ChatColor.GREEN + "Redetected players.");
    }

    @CommandAlias("ra")
    @Subcommand("rubius animation")
    @CommandCompletion("@rubiusanimation @bool")
    @Syntax("<animation> <loop>")
    public void onRubiusAnimation(Player player, String animationName, @Optional @Default("false") Boolean loop) {
        var manager = instance.getEntityModelManager();
        EntityModel model = manager.getEntityModel(player.getUniqueId());
        if (model == null || !Objects.equals(model.getActiveModel().getModelId(), "rubius")) {
            player.sendMessage(ChatColor.RED + "You are not disguised as a Rubius");
            return;
        }

        Animation animation = model.getAnimation(animationName);
        if (animation == null) {
            player.sendMessage(ChatColor.RED + "Animation not found");
            return;
        }

        try {
            model.playAnimation(animationName, loop);
        } catch (InvalidAnimationException e) {
            System.out.println("Invalid animation name: " + animationName);
        }
    }
}
