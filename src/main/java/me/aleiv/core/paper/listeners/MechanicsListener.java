package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Games.GlobalGame.Clothe;
import me.aleiv.core.paper.objects.Participant.Role;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MechanicsListener implements Listener {

    Core instance;

    public MechanicsListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoinUniform(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var equip = player.getEquipment();
        if (equip.getChestplate() == null) {
            var game = instance.getGame();
            if (game.getGameStage() != GameStage.LOBBY) {
                var participants = game.getParticipants();
                var uuid = player.getUniqueId().toString();
                var participant = participants.get(uuid);
                var global = game.getGlobalGame();
                var role = participant.getRole();

                if (role == Role.PLAYER) {
                    global.clothes(player, Clothe.UNIFORM);
                }
            }
        }

    }

}
