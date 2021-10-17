package me.aleiv.core.paper.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.aleiv.core.paper.Core;

public class GreenLightListener implements Listener {

    Core instance;

    public GreenLightListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        var game = instance.getGame();
        var player = e.getPlayer();
        //TODO: ADD IF IS INSIDE ARENA
        if (player.getGameMode() == GameMode.ADVENTURE && game.isPlayer(player)) {
            
            var panel = game.getMainGamePanel().getGreenLightPanel();
            var moved = panel.getPlayersMoved();
            if(!moved.contains(player)){
                moved.add(player);
                panel.updateMovedPlayers();
            }
        }

    }
}
