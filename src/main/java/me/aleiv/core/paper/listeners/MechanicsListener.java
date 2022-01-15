package me.aleiv.core.paper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Games.GlobalGame.Clothe;
import me.aleiv.core.paper.objects.Participant.Role;

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
            var participants = game.getParticipants();
            var uuid = player.getUniqueId().toString();
            var participant = participants.get(uuid);
            var role = participant.getRole();
            if(role == Role.PLAYER){
                if (game.getGameStage() != GameStage.LOBBY) {
                    var global = game.getGlobalGame();
    
                    global.clothes(player, Clothe.UNIFORM);
                    
                }

            }
            
        }

    }


}
