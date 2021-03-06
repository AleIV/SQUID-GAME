package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalStage.Stage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FrozeListener implements Listener {

    Core instance;

    public FrozeListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        var game = instance.getGame();
        var player = e.getPlayer();

        if (game.isPlayer(player)) {
            if (game.isPlayerFrozen(player.getUniqueId()) || game.isAllFroze()) {
                var from = e.getFrom();
                var to = e.getTo();
                var x1 = from.getX();
                var z1 = from.getZ();
                var x2 = to.getX();
                var z2 = to.getZ();

                var y1 = from.getY();
                var y2 = to.getY();
                if (x1 != x2 || z1 != z2) {
                    e.setCancelled(true);
                }
                var stage = game.getStage();
                if(stage == Stage.FINAL &&  y1 != y2){
                    e.setCancelled(true);
                }
            }
        }

    }
}
