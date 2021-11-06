package me.aleiv.core.paper.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import me.aleiv.core.paper.Core;

/**
 * @author DarkSeraphim.
 */
public class SkinChanger {

    private PacketAdapter adapter;

    private volatile Collection<WrappedSignedProperty> properties;

    public void onEnable() {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                realRun();
            }   

            public void realRun() {
                WrappedGameProfile profile = WrappedGameProfile.fromOfflinePlayer(Bukkit.getOfflinePlayer("Notch"));
                Object handle = profile.getHandle();
                Object sessionService = getSessionService();
                try {
                    Method method = getFillMethod(sessionService);
                    method.invoke(sessionService, handle, true);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    return;
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    return;
                }
                profile = WrappedGameProfile.fromHandle(handle);
                SkinChanger.this.properties = profile.getProperties().get("textures");
            }
        });

        // Intercept Player Info packets such that all players added to the tablist are
        // skinned as Notch.
        this.adapter = new PacketAdapter(Core.getInstance(), PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (SkinChanger.this.properties == null) {
                    return;
                }
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerInfoAction action = packet.getPlayerInfoAction().read(0);
                if (action != EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
                    return;
                }
                List<PlayerInfoData> data = packet.getPlayerInfoDataLists().read(0);
                for (PlayerInfoData pid : data) {
                    WrappedGameProfile profile = pid.getProfile();
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().putAll("textures", SkinChanger.this.properties);
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.adapter);
    }

    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this.adapter);
    }

    /**
     * Gets the mojang session service from Bukkit through reflection.
     * 
     * @return The mojang session service.
     */
    private Object getSessionService() {
        Server server = Bukkit.getServer();
        try {
            Object mcServer = server.getClass().getDeclaredMethod("getServer").invoke(server);
            for (Method m : mcServer.getClass().getMethods()) {
                if (m.getReturnType().getSimpleName().equalsIgnoreCase("MinecraftSessionService")) {
                    return m.invoke(mcServer);
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException("An error occurred while trying to get the session service", ex);
        }
        throw new IllegalStateException("No session service found :o");
    }

    private Method getFillMethod(Object sessionService) {
        for (Method m : sessionService.getClass().getDeclaredMethods()) {
            if (m.getName().equals("fillProfileProperties")) {
                return m;
            }
        }
        throw new IllegalStateException("No fillProfileProperties method found in the session service :o");
    }
}