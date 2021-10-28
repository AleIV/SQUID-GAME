package me.aleiv.core.paper.map.commands;

import java.io.File;
import java.util.List;

import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView.Scale;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.MapSystemManager;
import me.aleiv.core.paper.map.packet.WrapperPlayServerPlayerInfo;
import me.aleiv.core.paper.map.renderer.CustomRender;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;

@CommandAlias("map")
public class MapCommands extends BaseCommand {
    private MapSystemManager manager;

    public MapCommands(MapSystemManager manager) {
        this.manager = manager;
    }

    @Default
    public void creteMap(Player sender, String imageName) {
        var mapView = Bukkit.createMap(sender.getWorld());
        // Store the maps
        manager.getMap().put(mapView, sender.getUniqueId());
        // Remove all renderers
        mapView.getRenderers().forEach(renderer -> mapView.removeRenderer(renderer));
        // Disable tracking
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        mapView.setScale(Scale.FARTHEST);
        mapView.addRenderer(new CustomRender(imageName));

        var item = new ItemStack(Material.FILLED_MAP);
        var meta = item.getItemMeta();
        // Set the map to the object.
        if (meta instanceof MapMeta mapMeta) {
            mapMeta.setMapView(mapView);
        }
        item.setItemMeta(meta);
        // Give the player the map.
        sender.getInventory().addItem(item);
    }

    @Subcommand("mandar-a-la-verga")
    public void iterateThroughPixels(Player sender, int amount, int interval) {

        for (int i = 0; i < amount; i++) {
            Bukkit.getScheduler().runTaskLater(Core.getInstance(),
                    () -> manager.getMap().entrySet().stream().filter(owner -> owner.getValue() == sender.getUniqueId())
                            .forEach(map -> this.manager.updateMap(map.getKey(), sender)),
                    i * interval);

        }
    }

    @Subcommand("change-skin-nms")
    public void changeSkin(Player sender) {
        var texture = "ewogICJ0aW1lc3RhbXAiIDogMTYzNTM2MDkzNzMwNywKICAicHJvZmlsZUlkIiA6ICJiNzQ3OWJhZTI5YzQ0YjIzYmE1NjI4MzM3OGYwZTNjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlZXgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzRkZTFjYmI1OTBmM2M0YjEwZmM2ZTZlY2UwYjVhMGFiYjlhZWNlYTA3NGRjYTdkMWFjODQ1NjA4YTc3MyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
        var signature = "ixbrjewoW+wNl4XkFWBOg1ZzhOja9Un2afP2SpmRg+L0x4Rf6DNK9i6ff+Mm/gibVJLr8OOd5dQyrlUddZFRRSdmVixBnpy5AnOKbSB0zhsUXkpNUUbJyKroS689l7vKhAGZZHEAQS5HIW0kXY8FTulGD9s/ASTdX4S4PLrjDpeJrANQoBhVXPpk9qwJ+JcYQkfWeQXgran2eEigu8UNS51edxh65GH+Y6G4FkBLIoNPViItL5Y8Rz7EjCAqrJ/Y8p3x9rmHM+CuSPjW6QMWeGWNMOkAApuEZDiPUfe20EwQA+PtERHCZmgnuUOjYaEk4WZIhBGcyJW8EMk3XpOWX/JoDgYdwAlSRraDWJ0H8Fl3JDX5Af/3RrGjBVGPwI2on/8clDIr1ominanGlze9iqVFeHDOHQsbNeBwZrRbjiZMtwqXCxRl3B74FSTzbZLBWOJCbm7wtbbv0mp168J4kNY4LFkBc/vvhqtwqq8tOYqmFhWhO5DkF8c1GTqllj8AMZzLmxyXBzenPZaO6pjFWQhQWIuQTJ/AaMTH9Nzr5WDbTEEnq31Dpq92GiV5eLgH9g4UiJhMlgWmgmjdhW2miEg4RLeZtNQbJKeQ5LelZYzex5ctKsVfZJTyZeoLy81JW5tYTOdojV0C2hngu7LvBgzVE2Ps7/m7Z0Zf2BgCRLo=";
        var entityPlayer = ((CraftPlayer) sender.getPlayer()).getHandle();
        var prof = entityPlayer.getProfile();
        var con = entityPlayer.playerConnection;

        con.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        prof.getProperties().removeAll("textures");
        prof.getProperties().put("textures", new Property("textures", texture, signature));
        con.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

        var profilePurpur = sender.getPlayerProfile();
        profilePurpur.setProperty(new ProfileProperty(sender.getName(), texture, signature));

        sender.setPlayerProfile(profilePurpur);
        sender.setPlayerTime(0, true);
        sender.resetPlayerTime();

    }

    @Subcommand("change-skin-protocol")
    public void changeSkinProtocol(Player sender) {
        var texture = "ewogICJ0aW1lc3RhbXAiIDogMTYzNTM2MDkzNzMwNywKICAicHJvZmlsZUlkIiA6ICJiNzQ3OWJhZTI5YzQ0YjIzYmE1NjI4MzM3OGYwZTNjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlZXgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IwMzRkZTFjYmI1OTBmM2M0YjEwZmM2ZTZlY2UwYjVhMGFiYjlhZWNlYTA3NGRjYTdkMWFjODQ1NjA4YTc3MyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
        var signature = "ixbrjewoW+wNl4XkFWBOg1ZzhOja9Un2afP2SpmRg+L0x4Rf6DNK9i6ff+Mm/gibVJLr8OOd5dQyrlUddZFRRSdmVixBnpy5AnOKbSB0zhsUXkpNUUbJyKroS689l7vKhAGZZHEAQS5HIW0kXY8FTulGD9s/ASTdX4S4PLrjDpeJrANQoBhVXPpk9qwJ+JcYQkfWeQXgran2eEigu8UNS51edxh65GH+Y6G4FkBLIoNPViItL5Y8Rz7EjCAqrJ/Y8p3x9rmHM+CuSPjW6QMWeGWNMOkAApuEZDiPUfe20EwQA+PtERHCZmgnuUOjYaEk4WZIhBGcyJW8EMk3XpOWX/JoDgYdwAlSRraDWJ0H8Fl3JDX5Af/3RrGjBVGPwI2on/8clDIr1ominanGlze9iqVFeHDOHQsbNeBwZrRbjiZMtwqXCxRl3B74FSTzbZLBWOJCbm7wtbbv0mp168J4kNY4LFkBc/vvhqtwqq8tOYqmFhWhO5DkF8c1GTqllj8AMZzLmxyXBzenPZaO6pjFWQhQWIuQTJ/AaMTH9Nzr5WDbTEEnq31Dpq92GiV5eLgH9g4UiJhMlgWmgmjdhW2miEg4RLeZtNQbJKeQ5LelZYzex5ctKsVfZJTyZeoLy81JW5tYTOdojV0C2hngu7LvBgzVE2Ps7/m7Z0Zf2BgCRLo=";

        var removePacket = new WrapperPlayServerPlayerInfo();
        removePacket.setAction(PlayerInfoAction.REMOVE_PLAYER);
        removePacket.setData(List.of(new PlayerInfoData(WrappedGameProfile.fromPlayer(sender), sender.getPing(),
                NativeGameMode.fromBukkit(sender.getGameMode()), WrappedChatComponent.fromText(sender.getName()))));

        removePacket.sendPacket(sender);

        var addPacket = new WrapperPlayServerPlayerInfo();
        addPacket.setAction(PlayerInfoAction.ADD_PLAYER);

        var f = new GameProfile(sender.getUniqueId(), sender.getName());
        f.getProperties().removeAll("textures");
        f.getProperties().put("textures", new Property(sender.getName(), texture, signature));
        var skinnedProfile = WrappedGameProfile.fromHandle(f);

        addPacket.setData(List.of(new PlayerInfoData(skinnedProfile, sender.getPing(),
                NativeGameMode.fromBukkit(sender.getGameMode()), WrappedChatComponent.fromText(sender.getName()))));
        addPacket.sendPacket(sender);

        var profilePurpur = sender.getPlayerProfile();
        profilePurpur.setProperty(new ProfileProperty(sender.getName(), texture, signature));

        sender.setPlayerProfile(profilePurpur);
        sender.setPlayerTime(0, true);
        sender.resetPlayerTime();

    }

    @Subcommand("opencv")
    public void opencv(CommandSender sender) {
        nu.pattern.OpenCV.loadLocally();
        var basedir = System.getProperty("user.dir") + File.separatorChar + "assets" + File.separatorChar;
        createSkin(basedir + "skin.png", basedir + "mask.png", basedir + "out.png");
    }

    void createSkin(String fileLocation, String maskLocation, String outputLocation) {
        var skin = Imgcodecs.imread(fileLocation);

        var mask = Imgcodecs.imread(maskLocation);

        var output = new Mat();

        org.opencv.core.Core.bitwise_and(skin, skin, output, mask);

        Imgcodecs.imwrite(outputLocation, output);

    }

}
