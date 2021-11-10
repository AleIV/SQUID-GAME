package me.aleiv.core.paper.commands;

import java.util.List;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;

@CommandAlias("skin")
public class SkinCMD extends BaseCommand {

    public SkinCMD(Core instance) {
        instance.getCommandManager().getCommandCompletions().registerStaticCompletion("variants",
                List.of("civilian", "guard", "participant", "tux"));
        instance.getCommandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@variants")
    public void changeSkin(Player sender, String variant) {
        // Check if user has skins already, othewise create new one
        var previousSkins = SkinToolApi.getPlayerSkins(sender.getUniqueId());

        if (previousSkins != null) {
            var iterator = previousSkins.getAsJsonArray("skins").iterator();

            JsonObject finalSkin = null;

            while (iterator.hasNext()) {
                var skin = iterator.next().getAsJsonObject();
                var skinName = skin.get("name").getAsString();
                if (skinName.equalsIgnoreCase(variant)) {
                    finalSkin = skin;
                    break;
                }
            }

            if (finalSkin != null) {
                // Change the user's skin
                var value = finalSkin.get("value").getAsString();
                var signature = finalSkin.get("signature");
                // If signature is null, then the skin hasn't been signed onto mojang servers
                // yet!
                if (signature == null) {
                    sender.sendMessage("§cThis skin hasn't been signed onto Mojang servers yet!");
                    return;
                }
                // Apply the skin to the player
                skinSwapper(sender, value, signature.getAsString());

            } else {
                // Notify the user that the skin they requested doesn't exist
                sender.sendMessage(
                        Core.getMiniMessage().parse("<red>The skin <white>" + variant + "<red> does not exist!"));
            }

        } else {
            // Notify the user that they don't have any skins
            sender.sendMessage(Core.getMiniMessage()
                    .parse("<red>You don't have any skins! \n<red>Generate them using <white>/skin generate<red>!"));
        }

    }

    @Subcommand("generate")
    public void generateSkins(Player sender) {
        var previousSkins = SkinToolApi.getPlayerSkins(sender.getUniqueId());
        if (previousSkins != null) {
            sender.sendMessage(Core.getMiniMessage().parse("<red>You already have a skin!"));
            var iterator = previousSkins.getAsJsonArray("skins").iterator();

            while (iterator.hasNext()) {
                var skin = iterator.next().getAsJsonObject();
                var skinName = skin.get("name").getAsString();
                var signature = skin.get("signature");
                if (signature == null) {
                    sender.sendMessage(Core.getMiniMessage().parse("<red>Your skin variant " + "<white>" + skinName
                            + "<red> hasn't been signed onto Mojang servers yet!"));
                    return;
                }
            }
            return;
        } else {
            // Generate the skins
            var skinCollection = SkinToolApi.createPlayerSkins(sender.getUniqueId());
            if (skinCollection != null) {
                sender.sendMessage(Core.getMiniMessage().parse(
                        "<green>Your skins have been generated!\n<yellow>Please note that they still need to be signed by mojang, and that process might take upto <white>10<yellow> minutes."));
            } else {
                sender.sendMessage(Core.getMiniMessage().parse("<red>An error occured while generating your skins!"));
            }
        }
    }

    private void skinSwapper(Player player, String texture, String signature) {
        var entityPlayer = ((CraftPlayer) player.getPlayer()).getHandle();
        var prof = entityPlayer.getProfile();
        var con = entityPlayer.playerConnection;

        con.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        prof.getProperties().removeAll("textures");
        prof.getProperties().put("textures", new Property("textures", texture, signature));
        con.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

        var profilePurpur = player.getPlayerProfile();
        profilePurpur.setProperty(new ProfileProperty(player.getName(), texture, signature));

        player.setPlayerProfile(profilePurpur);
        player.setPlayerTime(0, true);
        player.resetPlayerTime();
    }

}
