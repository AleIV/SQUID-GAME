package me.aleiv.core.paper.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

public class NpcManager {

    void main() {
        var pManager = ProtocolLibrary.getProtocolManager();

        // Create a fake player info packet
        var pInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        pInfo.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);

    }

}
