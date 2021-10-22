package me.aleiv.core.paper.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.aleiv.core.paper.Core;

public class RopeListener implements Listener {

    Core instance;

    public RopeListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerInteractAtEntityEvent e){
        var entity = e.getRightClicked();
        if(entity instanceof ArmorStand stand){

        }
    }
}
