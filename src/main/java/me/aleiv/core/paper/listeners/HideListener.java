package me.aleiv.core.paper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aleiv.core.paper.Core;

public class HideListener implements Listener {

    Core instance;

    public HideListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        var game = instance.getGame();
        var hideMode = game.getHideMode();
        var player = e.getPlayer();

        switch (hideMode) {
            case INGAME ->{
                game.applyInGameHide(player);
            }
            case LOBBY ->{
                game.applyLobbyHide(player);
            }
            case TEST ->{
                return;
            }
        }
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent e){
        var game = instance.getGame();
        var hideMode = game.getHideMode();
        var player = e.getPlayer();

        switch (hideMode) {
            case INGAME ->{
                game.applyInGameHide(player);
            }
            case LOBBY ->{
                game.applyLobbyHide(player);
            }
            case TEST ->{
                return;
            }
        }

    }



}