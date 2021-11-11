package me.aleiv.core.paper.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import io.github.znetworkw.znpcservers.NPCLibrary;
import io.github.znetworkw.znpcservers.NPCWrapper;
import io.github.znetworkw.znpcservers.npc.NPC;
import me.aleiv.core.paper.Core;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;

/**
 * A command to interact with the skin-tool app from minecraft.
 * 
 * @author jcedeno
 */
@CommandAlias("skin")
public class SkinCMD extends BaseCommand {

    public SkinCMD(Core instance) {
        // register command completion
        instance.getCommandManager().getCommandCompletions().registerStaticCompletion("variants",
                List.of("civilian", "guard", "participant", "tux", "original"));
        // register the command itself
        instance.getCommandManager().registerCommand(this);
    }

    @Subcommand("npc-demo")
    public void npcDemo(Player sender, @Default("5") Integer seconds) {
        /** Generate npcs along a circle away from the player */
        double radius = 5;
        /** Get the location of the player and store the world as a constant. */
        final var loc = sender.getLocation();
        final var world = loc.getWorld();

        /** Obtain the skin variants available for the current sender. */
        SkinToolApi.getElseComputeSkins(sender.getUniqueId()).whenComplete((skins, exception) -> {
            if (exception != null) {
                sender.sendMessage("Command ended exceptionally: " + exception.getMessage());
                exception.printStackTrace();
            } else {
                try {

                    var t = 360 / skins.size();
                    var iter = skins.iterator();

                    var npcs = new ArrayList<NPC>();

                    for (int i = 0; i < 360; i += t) {
                        var angle = Math.toRadians(i);
                        // TODO Add distance instead of just degrees to the angle
                        var x = Math.cos(angle) * radius;
                        var z = Math.sin(angle) * radius;

                        // Get the new location
                        final var newLoc = loc.clone().add(x, 0, z);
                        // Change Y To Highest block.
                        newLoc.setY(world.getHighestBlockYAt(newLoc.getBlockX(), newLoc.getBlockZ()) + 1);
                        var next = iter.next();
                        // Spawn the NPC
                        npcs.add(NPCLibrary.createPlayerNPC(newLoc, sender.getName() + "-" + next.getName(), true,
                                sender.getInventory(), next.getValue(), next.getSignature()));

                    }

                    var wrapper = NPCWrapper.create(npcs);
                    // Delete the wrapper some seconds later.
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Core.getInstance(), wrapper::deleteAll,
                            20 * seconds);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }

    @Subcommand("set-other")
    @CommandCompletion("@players @players @variants")
    public void changeSkinOther(CommandSender sender, String playerTarget, String skinSourcePlayer,
            @Default("original") String variant) {
        var player = Bukkit.getPlayer(playerTarget);
        if (player != null && player.isOnline()) {
            UUID id = null;

            var ofP = Bukkit.getOfflinePlayerIfCached(skinSourcePlayer);

            if (ofP != null && ofP.getUniqueId() != null) {
                id = ofP.getUniqueId();
            } else {
                id = SkinToolApi.getUserProfile(skinSourcePlayer);
            }

            if (id != null) {
                // changeSkin(player, id.toString(), variant);
                if (variant.equalsIgnoreCase("original")) {
                    var skin = SkinToolApi.getCurrentUserSkin(id, false);
                    skinSwapper(player, skin.getValue(), skin.getSignature());

                } else {
                    SkinToolApi.getElseComputeSkins(id).whenComplete((skins, exception) -> {
                        if (exception != null) {
                            sender.sendMessage("Command ended exceptionally: " + exception.getMessage());
                            exception.printStackTrace();
                        } else {
                            var skin = skins.stream().filter(s -> s.getName().equalsIgnoreCase(variant)).findFirst();
                            if (skin.isPresent()) {
                                var actualSkin = skin.get();
                                Bukkit.getScheduler().runTask(Core.getInstance(),
                                        () -> skinSwapper(player, actualSkin.getValue(), actualSkin.getSignature()));

                            } else {
                                sender.sendMessage("Skin not found");
                            }
                        }

                    });
                }
            } else {
                sender.sendMessage("§cThe player you specified does not have a skin set.");
            }

        } else {
            sender.sendMessage("§cPlayer not found.");
        }

    }
    /*
     * 
     * @Default
     * 
     * @CommandCompletion("@variants") public void changeSkin(Player sender, String
     * uuid, String variant) { // Check if user has skins already, othewise create
     * new one
     * 
     * var previousSkins = SkinToolApi.getPlayerSkins(UUID.fromString(uuid));
     * 
     * if (previousSkins != null) { var iterator =
     * previousSkins.getAsJsonArray("skins").iterator();
     * 
     * JsonObject finalSkin = null;
     * 
     * while (iterator.hasNext()) { var skin = iterator.next().getAsJsonObject();
     * var skinName = skin.get("name").getAsString(); if
     * (skinName.equalsIgnoreCase(variant)) { finalSkin = skin; break; } }
     * 
     * if (finalSkin != null) { // Change the user's skin var value =
     * finalSkin.get("value").getAsString(); var signature =
     * finalSkin.get("signature"); // If signature is null, then the skin hasn't
     * been signed onto mojang servers // yet! if (signature == null) {
     * sender.sendMessage("§cThis skin hasn't been signed onto Mojang servers yet!"
     * ); return; } // Apply the skin to the player
     * Bukkit.getScheduler().runTask(Core.getInstance(), () -> skinSwapper(sender,
     * value, signature.getAsString()));
     * 
     * } else { // Notify the user that the skin they requested doesn't exist
     * sender.sendMessage( Core.getMiniMessage().parse("<red>The skin <white>" +
     * variant + "<red> does not exist!")); }
     * 
     * } else { // Notify the user that they don't have any skins
     * sender.sendMessage( Core.getMiniMessage().
     * parse("<red>You don't have any skins! \n<red>Generating them for you..."));
     * 
     * // ASk to generate the skins
     * SkinToolApi.createPlayerSkins(UUID.fromString(uuid)); // Recall this method
     * changeSkin(sender, uuid, variant); }
     * 
     * }
     * 
     * @Subcommand("generate") public void generateSkins(Player sender) { var
     * previousSkins = SkinToolApi.getPlayerSkins(sender.getUniqueId()); if
     * (previousSkins != null) { sender.sendMessage(Core.getMiniMessage().
     * parse("<red>You already have a skin!")); var iterator =
     * previousSkins.getAsJsonArray("skins").iterator();
     * 
     * while (iterator.hasNext()) { var skin = iterator.next().getAsJsonObject();
     * var skinName = skin.get("name").getAsString(); var signature =
     * skin.get("signature"); if (signature == null) {
     * sender.sendMessage(Core.getMiniMessage().parse("<red>Your skin variant " +
     * "<white>" + skinName + "<red> hasn't been signed onto Mojang servers yet!"));
     * return; } } return; } else { // Generate the skins var skinCollection =
     * SkinToolApi.createPlayerSkins(sender.getUniqueId()); if (skinCollection !=
     * null) { sender.sendMessage(Core.getMiniMessage().parse(
     * "<green>Your skins have been generated!\n<yellow>Please note that they still need to be signed by mojang, and that process might take upto <white>10<yellow> minutes."
     * )); } else { sender.sendMessage(Core.getMiniMessage().
     * parse("<red>An error occured while generating your skins!")); } } }
     * 
     */

    /**
     * Function that swaps a player's skin for a different one.
     * 
     * @param player    The player to swap the skin for.
     * @param texture   The texture to apply to the player.
     * @param signature The signature of the texture.
     */
    private void skinSwapper(Player player, String texture, String signature) {
        player.sendMessage(Core.getMiniMessage().parse("<yellow>Changing your skin..."));
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
        player.sendMessage(Core.getMiniMessage().parse("<green>Skin changed!"));
        player.setPlayerTime(0, true);
        // Reset player time 2 ticks later.
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> player.resetPlayerTime(), 2);
    }

}
