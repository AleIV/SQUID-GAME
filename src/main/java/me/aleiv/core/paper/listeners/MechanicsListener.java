package me.aleiv.core.paper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aleiv.cinematicCore.paper.events.CinematicFinishEvent;
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

    @EventHandler
    public void onFinish(CinematicFinishEvent e){
        var cine = e.getCinematicProgress().getScenes().get(0);
        if(cine.getName().contains("1")){
            var game = instance.getGame();
            game.setAllFroze(false);
        }
    }


}
