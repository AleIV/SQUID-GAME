package me.aleiv.core.paper.utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PlayerUtils {

    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public void forceHandSwing(Player player, boolean offHandSwing) {
        PacketContainer animation = protocolManager.
                createPacket(PacketType.Play.Client.ARM_ANIMATION, false);

        animation.getEntityModifier(player.getWorld()).write(0, player);
        animation.getIntegers().
                write(1, offHandSwing ? 3 : 0); // 0 arm swing, 3 offhand swing
        try {
            protocolManager.sendServerPacket(player, animation);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }

    }

}
