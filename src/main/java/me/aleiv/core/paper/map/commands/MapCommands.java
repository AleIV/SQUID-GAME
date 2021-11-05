package me.aleiv.core.paper.map.commands;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView.Scale;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.map.MapSystemManager;
import me.aleiv.core.paper.map.objects.AsyncCanvas;
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
        // Put it in the map to store it and use it later.
        manager.getCanvas().put(AsyncCanvas.of(mapView), sender.getUniqueId());
        // Give the player the map.
        sender.getInventory().addItem(item);
    }

    @Subcommand("toggle-rotation")
    public void toggleRotation(CommandSender sender) {
        this.manager.allowedRotation = !this.manager.allowedRotation;
        sender.sendMessage("Rotation allowed: " + !this.manager.allowedRotation);

    }

    private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    private static Gson gson = new Gson();

    @Subcommand("change-skin-nms")
    public void changeCommand(Player sender, String name, String type) {
        var uri = URI.create("http://104.207.147.190:8080/" + getUserProfile(name).toString());
        var request = HttpRequest.newBuilder(uri).header("accept", "application/json").build();
        CompletableFuture.supplyAsync(() -> {
            try {
                var response = client.send(request, BodyHandlers.ofString());
                return response.body();
            } catch (Exception e) {
                e.printStackTrace();
                return "{}";
            }

        }).thenAccept(result -> {
            var response = gson.fromJson(result, JsonObject.class);
            if (response.get(type) != null) {
                var skin = response.get(type).getAsJsonObject();
                sender.sendMessage("Skin recieved: " + skin.toString());
                if (skin != null) {
                    var actualSkinObject = skin.getAsJsonObject("data").getAsJsonObject("texture");
                    var texture = actualSkinObject.get("value").getAsString();
                    var signature = actualSkinObject.get("signature").getAsString();
                    Bukkit.getScheduler().runTask(Core.getInstance(), () -> changeSkin(sender, texture, signature));
                }
            }

        });

    }

    private void changeSkin(Player player, String texture, String signature) {
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

    private UUID getUserProfile(String name) {
        // https://api.ashcon.app/mojang/v2/user/

        var uri = URI.create("https://api.ashcon.app/mojang/v2/user/" + name);
        var request = HttpRequest.newBuilder(uri).header("accept", "application/json").build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            var json = gson.fromJson(response.body(), JsonObject.class);
            return UUID.fromString(json.get("uuid").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
